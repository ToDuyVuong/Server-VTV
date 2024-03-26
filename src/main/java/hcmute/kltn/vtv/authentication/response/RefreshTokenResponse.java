package hcmute.kltn.vtv.authentication.response;

import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenResponse extends ResponseAbstract {

    private String accessToken;


    public static RefreshTokenResponse refreshTokenResponse(String accessToken) {
        RefreshTokenResponse refreshTokenResponse = new RefreshTokenResponse();
        refreshTokenResponse.setAccessToken(accessToken);
        refreshTokenResponse.setStatus("Success");
        refreshTokenResponse.setMessage("Lấy access token thành công.");
        refreshTokenResponse.setCode(200);
        return refreshTokenResponse;
    }
}
