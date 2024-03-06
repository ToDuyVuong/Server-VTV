package hcmute.kltn.vtv.model.data.user.request;

import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressStatusRequest {

    private String username;

    private Long addressId;

    private Status status;

    public void validate() {

        if (username == null || username.isEmpty()) {
            throw new BadRequestException("Tài khoản không được để trống.");
        }

        if (addressId == null) {
            throw new BadRequestException("Địa chỉ không được để trống.");
        }

        if (status == null) {
            throw new BadRequestException("Trạng thái không được để trống.");
        }

        if (Status.isValidStatus(String.valueOf(status))) {
            throw new BadRequestException("Trạng thái không hợp lệ.");
        }
    }


}
