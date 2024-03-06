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

    private String province;

    private String district;

    private String ward;

    private String fullAddress;

    private String fullName;

    private String phone;

    private Status status;

    private String codeWard;

    public void validate() {

        if (username == null || username.isEmpty()) {
            throw new BadRequestException("Tài khoản không được để trống.");
        }

        if (province == null || province.isEmpty()) {
            throw new BadRequestException("Tỉnh/Thành phố không được để trống.");
        }

        if (district == null || district.isEmpty()) {
            throw new BadRequestException("Quận/Huyện không được để trống.");
        }

        if (ward == null || ward.isEmpty()) {
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

        if (codeWard == null || codeWard.isEmpty()) {
            throw new BadRequestException("Mã xã/phường không được để trống.");
        }


        trim();
    }

    public void trim() {
        this.username = this.username.trim();
        this.province = this.province.trim();
        this.district = this.district.trim();
        this.ward = this.ward.trim();
        this.fullAddress = this.fullAddress.trim();
        this.fullName = this.fullName.trim();
        this.phone = this.phone.trim();

    }

    public static AddressDTO convertUpdateRequestToDTO(AddressRequest addressRequest) {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setAddressId(addressRequest.getAddressId());
        addressDTO.setProvince(addressRequest.getProvince());
        addressDTO.setDistrict(addressRequest.getDistrict());
        addressDTO.setWard(addressRequest.getWard());
        addressDTO.setFullAddress(addressRequest.getFullAddress());
        addressDTO.setFullName(addressRequest.getFullName());
        addressDTO.setPhone(addressRequest.getPhone());
        addressDTO.setStatus(addressRequest.getStatus());
        return addressDTO;
    }

    public static AddressDTO convertRequestToDTO(AddressRequest addressRequest) {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setProvince(addressRequest.getProvince());
        addressDTO.setDistrict(addressRequest.getDistrict());
        addressDTO.setWard(addressRequest.getWard());
        addressDTO.setFullAddress(addressRequest.getFullAddress());
        addressDTO.setFullName(addressRequest.getFullName());
        addressDTO.setPhone(addressRequest.getPhone());
        return addressDTO;
    }
}
