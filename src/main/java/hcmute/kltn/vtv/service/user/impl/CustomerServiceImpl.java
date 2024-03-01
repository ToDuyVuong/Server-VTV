package hcmute.kltn.vtv.service.user.impl;

import hcmute.kltn.vtv.model.extra.Role;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.user.request.ChangePasswordRequest;
import hcmute.kltn.vtv.model.data.user.request.ForgotPasswordRequest;
import hcmute.kltn.vtv.model.data.user.request.ProfileCustomerRequest;
import hcmute.kltn.vtv.model.data.user.response.ForgotPasswordResponse;
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
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Customer getCustomerByUsername(String username) {
        return customerRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Tài khoản không tồn tại."));
    }

    @Override
    public ProfileCustomerResponse getProfileCustomer(String username) {
        if (username == null || username.isEmpty()) {
            throw new BadRequestException("Tài khoản không được để trống.");
        }
        Customer customer = getCustomerByUsername(username);

        return profileCustomerResponse(customer, "Lấy thông tin khách hàng thành công.", "OK");
    }

    @Override
    @Transactional
    public ProfileCustomerResponse updateProfileCustomer(ProfileCustomerRequest request) {
        request.validate();

        Customer customerUpdate = getCustomerByUsername(request.getUsername());
        customerUpdate.setBirthday(request.getBirthday());
        customerUpdate.setEmail(request.getEmail());
        customerUpdate.setFullName(request.getFullName());
        customerUpdate.setGender(request.isGender());
        customerUpdate.setUpdateAt(LocalDateTime.now());

        try {
            customerRepository.save(customerUpdate);

            return profileCustomerResponse(customerUpdate, "Cập nhật thông tin khách hàng thành công.", "Success");
        } catch (Exception e) {
            throw new BadRequestException("Cập nhật thông tin khách hàng thất bại!");
        }

    }

    @Override
    @Transactional
    public ProfileCustomerResponse changePassword(ChangePasswordRequest request) {
        request.validate();
        Customer customerUpdate = getCustomerByUsername(request.getUsername());

        boolean checkPassword = passwordEncoder.matches(request.getOldPassword(), customerUpdate.getPassword());
        if (!checkPassword) {
            throw new BadRequestException("Mật khẩu cũ không chính xác.");
        }

        customerUpdate.setPassword(passwordEncoder.encode(request.getNewPassword()));
        customerUpdate.setUpdateAt(LocalDateTime.now());

        try {
            customerRepository.save(customerUpdate);

            return profileCustomerResponse(customerUpdate, "Cập nhật mật khẩu của khách hàng thành công.", "Success");
        } catch (Exception e) {
            throw new BadRequestException("Cập nhật mật khẩu của khách hàng thất bại!");
        }

    }

    @Override
    @Transactional
    public ForgotPasswordResponse resetPassword(ForgotPasswordRequest request) {
        request.validate();
        otpService.verifyOtp(request.getUsername(), request.getOtp());

        Customer customerUpdate = getCustomerByUsername(request.getUsername());
        customerUpdate.setPassword(passwordEncoder.encode(request.getNewPassword()));
        customerUpdate.setUpdateAt(LocalDateTime.now());
        try {
            customerRepository.save(customerUpdate);

            return forgotPasswordResponse(customerUpdate.getUsername(), customerUpdate.getEmail());
        } catch (Exception e) {
            throw new InternalServerErrorException("Cài lại mật khẩu của tài khoản thất bại!");
        }

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


    private ProfileCustomerResponse profileCustomerResponse(Customer customer, String message, String status) {
        ProfileCustomerResponse response = new ProfileCustomerResponse();
        response.setCustomerDTO(CustomerDTO.convertEntityToDTO(customer));
        response.setMessage(message);
        response.setStatus(status);
        response.setCode(200);

        return response;
    }


    private ForgotPasswordResponse forgotPasswordResponse (String username, String email) {
        ForgotPasswordResponse response = new ForgotPasswordResponse();
        response.setUsername(username);
        response.setEmail(email);
        response.setMessage("Mật khẩu của tài khoản " + username + " đã được cài lại thành công.");
        response.setStatus("Success");
        response.setCode(200);

        return response;
    }



    @Override
    public boolean checkUsernameExist(String username) {
        return customerRepository.existsByUsername(username);
    }


}
