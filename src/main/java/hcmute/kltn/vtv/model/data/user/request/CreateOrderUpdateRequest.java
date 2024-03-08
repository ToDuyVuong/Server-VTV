package hcmute.kltn.vtv.model.data.user.request;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderUpdateRequest {

    private String username;
    private List<UUID> cartIds;
    private Long addressId;
    private Long voucherSystemId;
    private Long voucherShopId;
    private String note;
    private String paymentMethod;
    private String shippingMethod;

    public void validate() {
        if (cartIds == null || cartIds.isEmpty()) {
            throw new BadRequestException("Giỏ hàng không được để trống!");
        }

        if (addressId == null) {
            throw new BadRequestException("Địa chỉ không được để trống!");
        }

        if (paymentMethod == null || paymentMethod.isEmpty()) {
            throw new BadRequestException("Phương thức thanh toán không được để trống! ");
        }
        if (!paymentMethod.equals("COD")) {
            throw new BadRequestException("Phương thanh toán không hợp lệ! Hiện tai chỉ hỗ trợ COD");
        }

        if (shippingMethod == null || shippingMethod.isEmpty()) {
            throw new BadRequestException("Phương thức vận chuyển không được để trống.");
        }

        if (!shippingMethod.equals("GHN") &&
                !shippingMethod.equals("GHTK") &&
                !shippingMethod.equals("EXPRESS")) {
            throw new BadRequestException(
                    "Phương thức vận chuyển không hợp lệ! Hiện tại chỉ hỗ trợ GHN, GHTK, EXPRESS");
        }

        trim();
    }

    public void trim() {
        this.username = this.username.trim();
        this.note = this.note.trim();
        this.paymentMethod = this.paymentMethod.trim();
        this.shippingMethod = this.shippingMethod.trim();
    }

}
