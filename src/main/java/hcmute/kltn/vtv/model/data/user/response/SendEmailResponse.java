package hcmute.kltn.vtv.model.data.user.response;

import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SendEmailResponse extends ResponseAbstract {

    private String username;

    private String email;

    private long timeValid;


    public static SendEmailResponse sendEmailResponse(String username, String email, String message) {
        SendEmailResponse response = new SendEmailResponse();
        response.setUsername(username);
        response.setEmail(email);
        response.setMessage(message);
        response.setStatus("Success");
        response.setCode(200);
        response.setTimeValid(300);

        return response;
    }
}
