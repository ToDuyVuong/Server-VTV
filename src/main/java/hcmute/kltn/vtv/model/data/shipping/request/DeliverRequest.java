package hcmute.kltn.vtv.model.data.shipping.request;

import hcmute.kltn.vtv.authentication.request.RegisterRequest;
import hcmute.kltn.vtv.model.entity.shipping.Deliver;
import hcmute.kltn.vtv.model.extra.EmailValidator;
import hcmute.kltn.vtv.model.extra.TypeWork;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Data
@ToString
@RequiredArgsConstructor
public class DeliverRequest {

    private String phone;

    private String wardCode;

    private String fullAddress;

    private TypeWork typeWork;

    private Long providerId;

    private String districtCodeWork;

    private List<String> wardsCodeWork;

    private RegisterRequest registerCustomerRequest;


    public void validate() {
        if (this.phone == null || this.phone.isEmpty()) {
            throw new BadRequestException("Số điện thoại không được để trống.");
        }

        if (!Pattern.matches("[0-9]+", phone)) {
            throw new BadRequestException("Số điện thoại chỉ được chứa ký tự số.");
        }

        if (phone.length() < 9 || phone.length() > 11) {
            throw new BadRequestException("Số điện thoại không hợp lệ.");
        }



        if (this.wardCode == null || this.wardCode.isEmpty()) {
            throw new BadRequestException("Mã phường/xã không được để trống.");
        }
        if (this.fullAddress == null || this.fullAddress.isEmpty()) {
            throw new BadRequestException("Địa chỉ không được để trống.");
        }
        if (this.typeWork == null) {
            throw new BadRequestException("Loại công việc không được để trống.");
        }



        if (this.districtCodeWork == null || this.districtCodeWork.isEmpty()) {
            throw new BadRequestException("Mã quận/huyện làm việc không được để trống.");
        }
        if (this.wardsCodeWork == null || this.wardsCodeWork.isEmpty()) {
            throw new BadRequestException("Mã phường/xã làm việc không được để trống.");
        }
        if(hasDuplicates(this.wardsCodeWork)){
            throw new BadRequestException("Mã phường/xã làm việc không được trùng lặp.");
        }

        registerCustomerRequest.validate();

        trim();
    }


    public static boolean hasDuplicates(List<String> list) {
        Set<String> uniqueSet = new HashSet<>();

        for (String code : list) {
            // Nếu thêm vào được tức là chưa tồn tại trong Set, ngược lại là đã tồn tại (trùng lặp)
            if (!uniqueSet.add(code)) {
                return true; // Trùng lặp được tìm thấy
            }
        }

        return false; // Không có trùng lặp
    }

    public void trim() {
        this.phone = this.phone.trim();
        this.wardCode = this.wardCode.trim();
        this.fullAddress = this.fullAddress.trim();
        this.districtCodeWork = this.districtCodeWork.trim();
    }


    public static Deliver convertRequestToEntity(DeliverRequest deliverRequest) {
        Deliver deliver = new Deliver();
        deliver.setPhone(deliverRequest.getPhone());
        deliver.setFullAddress(deliverRequest.getFullAddress());
        deliver.setTypeWork(deliverRequest.getTypeWork());

        return deliver;
    }


}
