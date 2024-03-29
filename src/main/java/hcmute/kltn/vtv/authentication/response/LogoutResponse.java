package hcmute.kltn.vtv.authentication.response;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogoutResponse {

        private String status;
        private String message;
        private int code;
}
