package hcmute.kltn.vtv.authentication.service;

import hcmute.kltn.vtv.authentication.request.LoginRequest;
import hcmute.kltn.vtv.authentication.request.RegisterRequest;
import hcmute.kltn.vtv.authentication.response.LoginResponse;
import hcmute.kltn.vtv.authentication.response.LogoutResponse;
import hcmute.kltn.vtv.authentication.response.RefreshTokenResponse;
import hcmute.kltn.vtv.authentication.response.RegisterResponse;
import hcmute.kltn.vtv.model.entity.user.Customer;
import hcmute.kltn.vtv.model.extra.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface IAuthenticationService {
    RegisterResponse register(RegisterRequest customerRequest);

    Customer addNewCustomer(RegisterRequest customerRequest);

    LoginResponse login(LoginRequest loginRequest, HttpServletResponse response);

    LogoutResponse logout(String refreshToken,
                          String fcmToken,
                          HttpServletResponse response);

    RefreshTokenResponse refreshToken(
            String refreshToken,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException;

    boolean checkRole(String username, Role role);

    // LogoutResponse logout(HttpServletRequest request, HttpServletResponse
    // response);

}
