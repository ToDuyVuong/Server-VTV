package hcmute.kltn.vtv.authentication.service.impl;

import hcmute.kltn.vtv.authentication.request.LoginRequest;
import hcmute.kltn.vtv.authentication.request.RegisterRequest;
import hcmute.kltn.vtv.authentication.response.LoginResponse;
import hcmute.kltn.vtv.authentication.response.LogoutResponse;
import hcmute.kltn.vtv.authentication.response.RefreshTokenResponse;
import hcmute.kltn.vtv.authentication.response.RegisterResponse;
import hcmute.kltn.vtv.authentication.service.IAuthenticationService;
import hcmute.kltn.vtv.authentication.service.IJwtService;
import hcmute.kltn.vtv.model.dto.user.CustomerDTO;
import hcmute.kltn.vtv.model.entity.user.Customer;
import hcmute.kltn.vtv.model.entity.user.Token;
import hcmute.kltn.vtv.model.extra.Role;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.user.CustomerRepository;
import hcmute.kltn.vtv.repository.user.TokenRepository;
import hcmute.kltn.vtv.service.user.ICustomerService;
import hcmute.kltn.vtv.service.user.IMailService;
import hcmute.kltn.vtv.service.vtv.IFcmService;
import hcmute.kltn.vtv.service.wallet.ILoyaltyPointService;
import hcmute.kltn.vtv.util.exception.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements IAuthenticationService {

    @Value("${application.security.jwt.refresh-token.expiration}")
    private int refreshExpiration;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final IJwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    private final IFcmService fcmService;
    private final ICustomerService customerService;
    private final ILoyaltyPointService loyaltyPointService;
    private final IMailService mailService;


    @Override
    @Transactional
    public RegisterResponse register(RegisterRequest customerRequest) {
        customerRequest.validate();
        existingCustomer(customerRequest);
        Customer customer = createCustomer(customerRequest);

        try {
            customerRepository.save(customer);
            loyaltyPointService.addNewLoyaltyPointAfterRegister(customer.getUsername());
            mailService.activateAccountSendOtpToEmail(customer.getUsername());

            String message = "Đăng ký tài khoản khách hàng thành công, " +
                    "vui lòng kiểm tra email để kích hoạt tài khoản." +
                    "Mã kích hoạt của tài khoản: " + customer.getUsername() +
                    " đã được gửi đến email: " + customer.getEmail()
                    + "Nếu không nhận được email, vui lòng kiểm tra hòm thư rác. " +
                    "Mã kích hoạt có hiệu lực trong 5 phút. ";

            return RegisterResponse.registerResponse(customer.getUsername(), customer.getEmail(), message);
        } catch (Exception e) {
            throw new InternalServerErrorException("Đăng ký tài khoản khách hàng thất bại");
        }

    }


    @Override
    public Customer addNewCustomer(RegisterRequest customerRequest) {
        customerRequest.validate();
        existingCustomer(customerRequest);
        Customer customer = createCustomer(customerRequest);
        try {
            customerRepository.save(customer);
            mailService.activateAccountSendOtpToEmail(customer.getUsername());
            loyaltyPointService.addNewLoyaltyPointAfterRegister(customer.getUsername());

            return customer;
        } catch (Exception e) {
            throw new InternalServerErrorException("Thêm tài khoản khách hàng thất bại!" + e.getMessage());
        }
    }


    @Override
    public LoginResponse login(LoginRequest loginRequest, HttpServletResponse response) {
        loginRequest.validate();
        Customer customer = getCustomerByLogin(loginRequest);

        try {
            var jwtToken = jwtService.generateToken(customer);
            var refreshToken = jwtService.generateRefreshToken(customer);
            addNewToken(customer, refreshToken);
            fcmService.addNewFcmToken(loginRequest.getFcmToken(), customer.getUsername());
            Cookie cookie = addCookie(refreshToken, refreshExpiration);
            response.addCookie(cookie);

            return LoginResponse.loginResponse(customer, jwtToken, refreshToken);
        } catch (Exception e) {
            throw new InternalServerErrorException("Đăng nhập thất bại");
        }
    }


    @Override
    public RefreshTokenResponse refreshToken(String refreshToken, HttpServletRequest request,
                                             HttpServletResponse response)
            throws IOException {
        checkRefreshToken(refreshToken);
        String username = jwtService.extractUsername(refreshToken);

        if (username != null) {
            Customer customer = customerService.getCustomerByUsername(username);
            if (customer != null) {
                if (jwtService.isTokenValid(refreshToken, customer)) {
                    String accessToken = jwtService.generateToken(customer);
                    return RefreshTokenResponse.refreshTokenResponse(accessToken);
                }
            } else {
                throw new NotFoundException("Tài khoản không tồn tại.");
            }
        } else {
            throw new InternalServerErrorException("Lỗi xác thực token.");
        }
        return null;
    }


    @Override
    @Transactional
    public LogoutResponse logout(String refreshToken,
                                 String fcmToken,
                                 HttpServletResponse response) {
        try {
            revokeCustomerToken(refreshToken);
            fcmService.deleteFcmToken(fcmToken);
            SecurityContextHolder.clearContext();
            Cookie cookie = addCookie(null, 0);
            response.addCookie(cookie);

            return new LogoutResponse("Success", "Đăng xuất thành công", 200);
        } catch (Exception e) {
            throw new InternalServerErrorException("Đăng xuất thất bại");
        }
    }


    @Override
    public boolean checkRole(String username, Role role) {
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Tài khoản không kiềm tra quyền không tồn tại."));
        return customer.getRoles().contains(role);
    }


    private Token getTokenByRefreshToken(String refreshToken) {
        if (refreshToken == null) {
            throw new BadRequestException("Token không được null. Đăng xuất thất bại.");
        }

        return tokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new NotFoundException("Token không tồn tại. Đăng xuất thất bại."));
    }


    private Customer getCustomerByLogin(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        return customerRepository.findByUsernameAndStatus(loginRequest.getUsername(), Status.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Tài khoản không tồn tại."));
    }


    void existingCustomer(RegisterRequest customerRequest) {
        if (customerRepository.existsByUsername(customerRequest.getUsername())) {
            throw new DuplicateEntryException("Tài khoản đã tồn tại.");
        }
        if (customerRepository.existsByEmail(customerRequest.getEmail())) {
            throw new DuplicateEntryException("Email đã được đăng ký.");
        }
    }


    private Customer createCustomer(RegisterRequest customerRequest) {

        Set<Role> roles = new HashSet<>();
        roles.add(Role.CUSTOMER);

        Customer customer = new Customer();
        customer.setUsername(customerRequest.getUsername());
        customer.setEmail(customerRequest.getEmail());
        customer.setGender(customerRequest.isGender());
        customer.setFullName(customerRequest.getFullName());
        customer.setBirthday(customerRequest.getBirthday());
        customer.setRoles(roles);
        customer.setPassword(passwordEncoder.encode(customerRequest.getPassword()));
        customer.setCreateAt(LocalDateTime.now());
        customer.setUpdateAt(LocalDateTime.now());
        customer.setStatus(Status.INACTIVE);

        return customer;
    }


    private Cookie addCookie(String value, int maxAge) {
        Cookie cookie = new Cookie("refreshToken", value);
        cookie.setHttpOnly(true);
        cookie.setPath("/"); // Đặt đúng path mà bạn muốn
        cookie.setMaxAge(maxAge); // Set thời gian sống của cookie (ví dụ: 30 ngày)
        return cookie;
    }


    public void addNewToken(Customer customer, String jwtToken) {
        Token token = Token.builder()
                .customer(customer)
                .token(jwtToken)
                .tokenType("BEARER")
                .expired(false)
                .revoked(false)
                .build();

        try {
            tokenRepository.save(token);
        } catch (Exception e) {
            throw new InternalServerErrorException("Lưu token thất bại");
        }
    }


    public void revokeCustomerToken(String token) {
        Token validToken = getTokenByRefreshToken(token);
        if (validToken.isExpired() || validToken.isRevoked()) {
            return;
        }
        validToken.setExpired(true);
        validToken.setRevoked(true);

        try {
            tokenRepository.save(validToken);
        } catch (Exception e) {
            throw new InternalServerErrorException("Xóa token thất bại");
        }
    }


    private void checkRefreshToken(String refreshToken) {
        if (refreshToken == null) {
            throw new BadRequestException("Refresh token không tồn tại.");
        }

        if (jwtService.isTokenExpired(refreshToken)) {
            throw new BadRequestException("Refresh token đã hết hạn.");
        }
    }

}
