package hcmute.kltn.vtv.authentication.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import hcmute.kltn.vtv.model.dto.user.CustomerDTO;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse extends ResponseAbstract {

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;

    private CustomerDTO customerDTO;



}
