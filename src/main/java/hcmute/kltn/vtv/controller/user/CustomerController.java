package hcmute.kltn.vtv.controller.user;

import hcmute.kltn.vtv.authentication.response.RegisterResponse;
import hcmute.kltn.vtv.model.data.user.request.ActiveAccountRequest;
import hcmute.kltn.vtv.model.data.user.request.ChangePasswordRequest;
import hcmute.kltn.vtv.model.data.user.request.ForgotPasswordRequest;
import hcmute.kltn.vtv.model.data.user.request.ProfileCustomerRequest;
import hcmute.kltn.vtv.model.data.user.response.ProfileCustomerResponse;
import hcmute.kltn.vtv.model.data.user.response.SendEmailResponse;
import hcmute.kltn.vtv.service.user.ICustomerService;
import hcmute.kltn.vtv.service.user.IMailService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {

    @Autowired
    private final ICustomerService customerService;
    @Autowired
    private final IMailService mailService;


    @GetMapping("/profile")
    public ResponseEntity<ProfileCustomerResponse> getProfileCustomer(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        ProfileCustomerResponse profileCustomerResponse = customerService.getProfileCustomer(username);

        return ResponseEntity.ok(profileCustomerResponse);
    }


    @PutMapping("/profile")
    public ResponseEntity<ProfileCustomerResponse> updateProfileCustomer(
            @RequestBody ProfileCustomerRequest profileCustomerRequest,
            HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        profileCustomerRequest.setUsername(username);
        ProfileCustomerResponse profileCustomerResponse = customerService.updateProfileCustomer(profileCustomerRequest);
        return ResponseEntity.ok(profileCustomerResponse);
    }


    @PatchMapping("/change-password")
    public ResponseEntity<ProfileCustomerResponse> changePassword(
            @RequestBody ChangePasswordRequest changePasswordRequest,
            HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        changePasswordRequest.setUsername(username);
        ProfileCustomerResponse profileCustomerResponse = customerService.changePassword(changePasswordRequest);

        return ResponseEntity.ok(profileCustomerResponse);
    }



    @GetMapping("/forgot-password")
    public ResponseEntity<SendEmailResponse> sendMailForgotPassword(@RequestParam("username") String username) {
        if (username == null || username.isEmpty()) {
            throw new BadRequestException("Tài khoản không được để trống.");
        }

        SendEmailResponse response = mailService.forgotPasswordSendOtpToEmail(username);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/active-account/send-email")
    public ResponseEntity<SendEmailResponse> sendEmailActivateAccount(@RequestParam("username") String username) {
        if (username == null || username.isEmpty()) {
            throw new BadRequestException("Tài khoản không được để trống.");
        }
        customerService.checkAccountUnActive(username);

        return ResponseEntity.ok(mailService.activateAccountSendOtpToEmail(username));
    }

    @PostMapping("/active-account")
    public ResponseEntity<RegisterResponse> activateAccount(@RequestBody ActiveAccountRequest request) {
        request.validate();

        return ResponseEntity.ok(customerService.activateAccount(request));
    }



    @PatchMapping("/reset-password")
    public ResponseEntity<RegisterResponse> resetPassword(@RequestBody ForgotPasswordRequest request) {
        RegisterResponse response = customerService.resetPassword(request);

        return ResponseEntity.ok(response);
    }


}