package hcmute.kltn.vtv.service.user.impl;

import hcmute.kltn.vtv.authentication.response.RegisterResponse;
import hcmute.kltn.vtv.model.data.user.request.ActiveAccountRequest;
import hcmute.kltn.vtv.model.extra.Role;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.user.request.ChangePasswordRequest;
import hcmute.kltn.vtv.model.data.user.request.ForgotPasswordRequest;
import hcmute.kltn.vtv.model.data.user.request.ProfileCustomerRequest;
import hcmute.kltn.vtv.model.data.user.response.SendEmailResponse;
import hcmute.kltn.vtv.model.data.user.response.ProfileCustomerResponse;
import hcmute.kltn.vtv.model.dto.user.CustomerDTO;
import hcmute.kltn.vtv.model.entity.user.Customer;
import hcmute.kltn.vtv.repository.user.CustomerRepository;
import hcmute.kltn.vtv.service.user.ICustomerService;
import hcmute.kltn.vtv.service.user.IOtpService;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements ICustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private IOtpService otpService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public Customer getCustomerByUsername(String username) {
        return customerRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Tài khoản không tồn tại."));
    }

    @Override
    public ProfileCustomerResponse getProfileCustomer(String username) {
        Customer customer = getCustomerByUsername(username);

        return ProfileCustomerResponse.profileCustomerResponse(customer, "Lấy thông tin khách hàng thành công.", "OK");
    }

    @Override
    @Transactional
    public ProfileCustomerResponse updateProfileCustomer(ProfileCustomerRequest request) {
        Customer customer = editProfileCustomerByProfileCustomerRequest(request);

        try {
            customerRepository.save(customer);

            return ProfileCustomerResponse.profileCustomerResponse(customer, "Cập nhật thông tin khách hàng thành công.", "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Cập nhật thông tin khách hàng thất bại!");
        }
    }

    @Override
    @Transactional
    public ProfileCustomerResponse changePassword(ChangePasswordRequest request) {
        Customer customerUpdate = getCustomerByUsername(request.getUsername());
        if (!passwordEncoder.matches(request.getOldPassword(), customerUpdate.getPassword())) {
            throw new InternalServerErrorException("Mật khẩu cũ không chính xác.");
        }
        customerUpdate.setPassword(passwordEncoder.encode(request.getNewPassword()));
        customerUpdate.setUpdateAt(LocalDateTime.now());
        try {
            customerRepository.save(customerUpdate);

            return ProfileCustomerResponse.profileCustomerResponse(customerUpdate, "Cập nhật mật khẩu của khách hàng thành công.", "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Cập nhật mật khẩu của khách hàng thất bại!");
        }

    }

    @Override
    @Transactional
    public RegisterResponse resetPassword(ForgotPasswordRequest request) {
        otpService.verifyOtp(request.getUsername(), request.getOtp());

        Customer customerUpdate = getCustomerByUsername(request.getUsername());
        if (customerUpdate.getStatus().equals(Status.INACTIVE)) {
            customerUpdate.setStatus(Status.ACTIVE);
        }
        customerUpdate.setPassword(passwordEncoder.encode(request.getNewPassword()));
        customerUpdate.setUpdateAt(LocalDateTime.now());
        try {
            customerRepository.save(customerUpdate);

            return RegisterResponse.registerResponse(
                    customerUpdate.getUsername(),
                    customerUpdate.getEmail(),
                    "Cài lại mật khẩu của tài khoản thành công.");
        } catch (Exception e) {
            throw new InternalServerErrorException("Cài lại mật khẩu của tài khoản thất bại!");
        }

    }


    @Override
    @Transactional
    public RegisterResponse activateAccount(ActiveAccountRequest request) {
        otpService.verifyOtp(request.getUsername(), request.getOtp());
        Customer customerUpdate = getCustomerByUsername(request.getUsername());
        customerUpdate.setStatus(Status.ACTIVE);
        customerUpdate.setUpdateAt(LocalDateTime.now());
        try {
            customerRepository.save(customerUpdate);

            return RegisterResponse.registerResponse(
                    customerUpdate.getUsername(),
                    customerUpdate.getEmail(),
                    "Kích hoạt tài khoản thành công.");
        } catch (Exception e) {
            throw new InternalServerErrorException("Kích hoạt tài khoản thất bại!");
        }
    }


    @Override
    public void checkAccountUnActive(String username) {
        Customer customer = getCustomerByUsername(username);
        if (customer.getStatus().equals(Status.INACTIVE)) {
            return;
        }
        throw new BadRequestException("Tài khoản của bạn đã được kích hoạt.");

    }


    @Override
    public boolean checkUsernameExist(String username) {
        return customerRepository.existsByUsername(username);
    }

    @Override
    public Customer getCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Khách hàng không tồn tại."));
    }


    @Override
    @Transactional
    public void removeAllRoleManagerOfCustomer(Customer customer) {

        Set<Role> roles = customer.getRoles();
        roles.removeAll(Arrays.asList(
                Role.MANAGERSHIPPING,
                Role.MANAGERCUSTOMER,
                Role.MANAGERVENDOR));

        try {
            customerRepository.save(customer);
        } catch (Exception e) {
            throw new InternalServerErrorException("Xóa quyền quản lý trong customer thất bại!");
        }


    }


    private Customer editProfileCustomerByProfileCustomerRequest(ProfileCustomerRequest request) {
        Customer customer = getCustomerByUsername(request.getUsername());
        customer.setBirthday(request.getBirthday());
        customer.setEmail(request.getEmail());
        customer.setFullName(request.getFullName());
        customer.setGender(request.isGender());
        customer.setUpdateAt(LocalDateTime.now());

        return customer;
    }






}
