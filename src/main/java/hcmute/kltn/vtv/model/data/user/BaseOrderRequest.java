package hcmute.kltn.vtv.model.data.user;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseOrderRequest {

    private Long addressId;
    private String systemVoucherCode;
    private String shopVoucherCode;
    private String paymentMethod;
    private String shippingMethod;
    private String note;
    private boolean useLoyaltyPoint;

    public void validate() {
        baseValidate();
    }

    protected void baseValidate() {
        if (this.addressId == null) {
            throw new BadRequestException("Địa chỉ không được để trống!");
        }

        if (paymentMethod == null || paymentMethod.isEmpty()) {
            throw new BadRequestException("Phương thức thanh toán không được để trống! ");
        }

        if (!paymentMethod.equals("COD")) {
            throw new BadRequestException("Phương thanh toán không hợp lệ! Hiện tại chỉ hỗ trợ COD");
        }

        if (shippingMethod == null || shippingMethod.isEmpty()) {
            throw new BadRequestException("Phương thức vận chuyển không được để trống.");
        }

        if (!shippingMethod.equals("GHN") &&
                !shippingMethod.equals("GHTK") &&
                !shippingMethod.equals("VTV Express")) {
            throw new BadRequestException(
                    "Phương thức vận chuyển không hợp lệ! Hiện tại chỉ hỗ trợ GHN, GHTK, VTV Express");
        }

        trim();
    }

    public void trim() {
        this.paymentMethod = this.paymentMethod.trim();
        this.shippingMethod = this.shippingMethod.trim();
        this.note = this.note.trim();
        if (this.systemVoucherCode != null) {
            this.systemVoucherCode = this.systemVoucherCode.trim();
        }
        if (this.shopVoucherCode != null) {
            this.shopVoucherCode = this.shopVoucherCode.trim();
        }
    }
}
