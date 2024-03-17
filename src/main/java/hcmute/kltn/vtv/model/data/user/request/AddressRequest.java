package hcmute.kltn.vtv.model.data.user.request;

import hcmute.kltn.vtv.model.dto.user.AddressDTO;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.*;

import java.util.regex.Pattern;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest {

    private String username;

    private Long addressId;

    private String provinceName;

    private String districtName;

    private String wardName;

    private String fullAddress;

    private String fullName;

    private String phone;

    private String wardCode;

    public void validate() {

        if (username == null || username.isEmpty()) {
            throw new BadRequestException("Tài khoản không được để trống.");
        }

        if (provinceName == null || provinceName.isEmpty()) {
            throw new BadRequestException("Tỉnh/Thành phố không được để trống.");
        }

        if (districtName == null || districtName.isEmpty()) {
            throw new BadRequestException("Quận/Huyện không được để trống.");
        }

        if (wardName == null || wardName.isEmpty()) {
            throw new BadRequestException("Phường/Xã không được để trống.");
        }

        if (fullAddress == null || fullAddress.isEmpty()) {
            throw new BadRequestException("Địa chỉ không được để trống.");
        }

        if (fullName == null || fullName.isEmpty()) {
            throw new BadRequestException("Họ tên không được để trống.");
        }

        if (phone == null || phone.isEmpty()) {
            throw new BadRequestException("Số điện thoại không được để trống.");
        }

        if (!Pattern.matches("[0-9]+", phone)) {
            throw new BadRequestException("Số điện thoại chỉ được chứa ký tự số.");
        }

        if (phone.length() < 9 || phone.length() > 11) {
            throw new BadRequestException("Số điện thoại không hợp lệ.");
        }

        if (wardCode == null || wardCode.isEmpty()) {
            throw new BadRequestException("Mã xã/phường không được để trống.");
        }


        trim();
    }

    public void trim() {
        this.username = this.username.trim();
        this.provinceName = this.provinceName.trim();
        this.districtName = this.districtName.trim();
        this.wardName = this.wardName.trim();
        this.fullAddress = this.fullAddress.trim();
        this.fullName = this.fullName.trim();
        this.phone = this.phone.trim();

    }
}
