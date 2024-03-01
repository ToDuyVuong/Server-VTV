package hcmute.kltn.vtv.service.user;

import hcmute.kltn.vtv.authentication.response.RegisterResponse;
import hcmute.kltn.vtv.model.data.user.request.ActiveAccountRequest;
import hcmute.kltn.vtv.model.data.user.request.ChangePasswordRequest;
import hcmute.kltn.vtv.model.data.user.request.ForgotPasswordRequest;
import hcmute.kltn.vtv.model.data.user.request.ProfileCustomerRequest;
import hcmute.kltn.vtv.model.data.user.response.SendEmailResponse;
import hcmute.kltn.vtv.model.data.user.response.ProfileCustomerResponse;
import hcmute.kltn.vtv.model.entity.user.Customer;
import org.springframework.transaction.annotation.Transactional;

public interface ICustomerService {
    Customer getCustomerByUsername(String username);

    ProfileCustomerResponse getProfileCustomer(String token);

    ProfileCustomerResponse updateProfileCustomer(ProfileCustomerRequest profileCustomerRequest);

    ProfileCustomerResponse changePassword(ChangePasswordRequest request);

    RegisterResponse resetPassword(ForgotPasswordRequest request);

    @Transactional
    RegisterResponse activateAccount(ActiveAccountRequest request);

    void checkAccountUnActive(String username);

    Customer getCustomerById(Long customerId);

    @Transactional
    void removeAllRoleManagerOfCustomer(Customer customer);

    boolean checkUsernameExist(String username);
}
