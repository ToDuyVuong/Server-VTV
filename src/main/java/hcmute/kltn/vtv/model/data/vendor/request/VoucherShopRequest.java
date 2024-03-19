package hcmute.kltn.vtv.model.data.vendor.request;

import hcmute.kltn.vtv.model.entity.vendor.Voucher;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.model.extra.VoucherType;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@ToString
@RequiredArgsConstructor
public class VoucherShopRequest {

    private Long voucherId;

    private String code;

    private String name;

    private String description;

    private int discount;

    private int quantity;

    private Date startDate;

    private Date endDate;

    private String type;

    private String username;

    public void validate() {
        if (this.code == null || this.code.isEmpty()) {
            throw new BadRequestException("Mã giảm giá không được để trống");
        }
        if (this.name == null || this.name.isEmpty()) {
            throw new BadRequestException("Tên giảm giá không được để trống");
        }
        if (this.description == null || this.description.isEmpty()) {
            throw new BadRequestException("Mô tả giảm giá không được để trống");
        }

        if (this.discount < 0) {
            throw new BadRequestException("Giá trị giảm giá không được nhỏ hơn 0%");
        }
        if (this.discount == 0) {
            throw new BadRequestException("Giá trị giảm giá không được bằng 0%");
        }
        if (this.quantity < 0) {
            throw new BadRequestException("Số lượng giảm giá không được nhỏ hơn 0");
        }

        if (this.quantity > 1000) {
            throw new BadRequestException("Số lượng giảm giá không được lớn hơn 1000");
        }
        if (this.startDate == null) {
            throw new BadRequestException("Ngày bắt đầu không được để trống");
        }
        if (this.endDate == null) {
            throw new BadRequestException("Ngày kết thúc không được để trống");
        }

        if (this.startDate.after(this.endDate)) {
            throw new BadRequestException("Ngày bắt đầu không được sau ngày kết thúc");
        }
        if (this.startDate.after(new Date())) {
            throw new BadRequestException("Ngày bắt đầu không được sau ngày hiện tại");
        }


    }

    public void validateCreate() {
        validate();

        if (this.type == null || this.type.isEmpty()) {
            throw new BadRequestException("Loại giảm giá không được để trống");
        }
        if (!this.type.equals("percent".trim()) && !this.type.equals("money".trim())) {
            throw new BadRequestException(
                    "Loại giảm giá không hợp lệ. Loại giảm giá cửa chỉ có thể là percent hoặc money");
        }
        if (this.type.equals("percent") && this.discount > 100) {
            throw new BadRequestException("Giá trị giảm giá không được lớn hơn 100%");
        }


        trim();
    }

    public void validateUpdate() {

        if (this.voucherId == null) {
            throw new BadRequestException("Mã giảm giá không được để trống");
        }

        validateCreate();
    }

    public static Voucher convertCreateToVoucher(VoucherShopRequest request) {
        Voucher voucher = new Voucher();
        voucher.setCode(request.getCode());
        voucher.setName(request.getName());
        voucher.setDescription(request.getDescription());
        voucher.setDiscount(request.getDiscount());
        voucher.setQuantity(request.getQuantity());
        voucher.setStartDate(request.getStartDate());
        voucher.setEndDate(request.getEndDate());
        voucher.setCreateAt(LocalDateTime.now());
        voucher.setUpdateAt(LocalDateTime.now());
        voucher.setQuantityUsed(0);
        voucher.setStatus(Status.ACTIVE);

        voucher.setType(convertType(request.getType()));

        return voucher;
    }

    public static Voucher convertUpdateToVoucher(VoucherShopRequest request, Voucher voucher) {
        voucher.setName(request.getName());
        voucher.setDescription(request.getDescription());
        voucher.setDiscount(request.getDiscount());
        voucher.setQuantity(request.getQuantity());
        voucher.setStartDate(request.getStartDate());
        voucher.setEndDate(request.getEndDate());
        voucher.setUpdateAt(LocalDateTime.now());
        voucher.setType(convertType(request.getType()));

        return voucher;
    }

    private static VoucherType convertType(String type) {
        if (type.equals("percent")) {
            return VoucherType.PERCENTAGE_SHOP;
        } else {
            return VoucherType.MONEY_SHOP;
        }
    }

    public void trim() {
        this.code = this.code.trim();
        this.name = this.name.trim();
        this.description = this.description.trim();
        this.type = this.type.trim();
        this.username = this.username.trim();
    }

}
