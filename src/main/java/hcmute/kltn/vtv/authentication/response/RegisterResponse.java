package hcmute.kltn.vtv.authentication.response;

import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse extends ResponseAbstract {

    private String username;
    private String email;



    public static RegisterResponse registerResponse(String username, String email, String message) {
        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setUsername(username);
        registerResponse.setEmail(email);
        registerResponse.setMessage(message);
        registerResponse.setStatus("Success");
        registerResponse.setCode(200);

        return registerResponse;
    }
}
