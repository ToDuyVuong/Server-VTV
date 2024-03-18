package hcmute.kltn.vtv.model.data.vendor.request;

import hcmute.kltn.vtv.model.extra.EmailValidator;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.regex.Pattern;

@Data
@ToString
@RequiredArgsConstructor
public class ShopRequest {

    private String name;

    private String address;

    private String provinceName;

    private String districtName;

    private String wardName;

    private String wardCode;

    private String phone;

    private String email;

    private MultipartFile avatar;

    private boolean changeAvatar;

    private String description;

    private String openTime;

    private String closeTime;

    public void validate() {
        if (this.name == null || this.name.isEmpty()) {
            throw new BadRequestException("Tên cửa hàng không được để trống!");
        }

        if (this.address == null || this.address.isEmpty()) {
            throw new BadRequestException("Địa chỉ cửa hàng không được để trống!");
        }

        if (this.provinceName == null || this.provinceName.isEmpty()) {
            throw new BadRequestException("Tỉnh/Thành phố không được để trống!");
        }

        if (this.districtName == null || this.districtName.isEmpty()) {
            throw new BadRequestException("Quận/Huyện không được để trống!");
        }

        if (this.wardName == null || this.wardName.isEmpty()) {
            throw new BadRequestException("Phường/Xã không được để trống!");
        }

        if (this.wardCode == null || this.wardCode.isEmpty()) {
            throw new BadRequestException("Mã phường/Xã không được để trống!");
        }

        if (this.phone == null || this.phone.isEmpty()) {
            throw new BadRequestException("Số điện thoại cửa hàng không được để trống!");
        }

        if (!Pattern.matches("[0-9]+", this.getPhone())) {
            throw new BadRequestException("Số điện thoại chỉ được chứa ký tự số.");
        }

        if (this.email == null || this.email.isEmpty()) {
            throw new BadRequestException("Email cửa hàng không được để trống!");
        }

        if (!EmailValidator.isValidEmail(email)) {
            throw new BadRequestException("Email không hợp lệ.");
        }

        if (this.openTime == null || this.openTime.isEmpty()) {
            throw new BadRequestException("Giờ mở cửa không được để trống!");
        }

        if (this.closeTime == null || this.closeTime.isEmpty()) {
            throw new BadRequestException("Giờ đóng cửa không được để trống!");
        }

        if (this.description == null || this.description.isEmpty()) {
            throw new BadRequestException("Mô tả cửa hàng không được để trống!");
        }

        if (this.avatar == null || this.avatar.isEmpty()) {
            throw new BadRequestException("Ảnh đại diện không được để trống!");
        }

        if (!Pattern.matches("[0-9]+", this.getPhone())) {
            throw new BadRequestException("Số điện thoại chỉ được chứa ký tự số.");
        }

        if (this.getPhone().length() < 9 || this.getPhone().length() > 11) {
            throw new BadRequestException("Số điện thoại không hợp lệ.");
        }

        if (this.changeAvatar && (this.avatar == null || this.avatar.isEmpty())) {
            throw new BadRequestException("Không thể thay đổi ảnh đại diện khi không có ảnh mới!");
        }

        if (this.changeAvatar && !isImage(this.avatar)) {
            throw new BadRequestException("Ảnh đại diện không hợp lệ! Vui lòng chọn ảnh khác!");
        }

        trim();
    }


    private boolean isImage(MultipartFile file) {
        return file != null && file.getContentType() != null && file.getContentType().startsWith("image");
    }

    public void trim() {
        this.name = this.name.trim();
        this.address = this.address.trim();
        this.phone = this.phone.trim();
        this.email = this.email.trim();
        this.description = this.description.trim();
        this.openTime = this.openTime.trim();
        this.closeTime = this.closeTime.trim();
        this.provinceName = this.provinceName.trim();
        this.districtName = this.districtName.trim();
        this.wardName = this.wardName.trim();
        this.wardCode = this.wardCode.trim();
    }

}
