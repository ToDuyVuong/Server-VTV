package hcmute.kltn.vtv.model.data.user.request;


import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.*;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ActiveAccountRequest {

    private String username;

    private String otp;

    public void validate() {
        if (username == null || username.isEmpty()) {
            throw new BadRequestException("Tài khoản không được để trống.");
        }

        if (otp == null || otp.isEmpty()) {
            throw new BadRequestException("Mã OTP không được để trống.");
        }

        trim();
    }

    public void trim() {
        this.username = this.username.trim();
        this.otp = this.otp.trim();
    }

}
