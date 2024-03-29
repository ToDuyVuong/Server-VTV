package hcmute.kltn.vtv.model.data.user.request;

import hcmute.kltn.vtv.model.data.user.BaseOrderRequest;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestWithCart extends BaseOrderRequest {

    private List<UUID> cartIds;

    public static void validate(OrderRequestWithCart request) {
        checkDuplicateCartIds(request.getCartIds());

        if (request.getCartIds() == null || request.getCartIds().isEmpty()) {
            throw new BadRequestException("Danh sách giỏ hàng không được để trống!");
        }

        request.validate();
    }

    public static void checkDuplicateCartIds(List<UUID> cartIds) {
        Set<UUID> set = new HashSet<>(cartIds);
        if (set.size() < cartIds.size()) {
            throw new BadRequestException("Danh giỏ hàng không được trùng lặp!");
        }
    }


    public static OrderRequestWithCart convertWithProductVariantToWithCart(OrderRequestWithProductVariant request) {
        OrderRequestWithCart orderRequestWithCart = new OrderRequestWithCart();
//        withCart.setCartIds(request.getCartIds());
        orderRequestWithCart.setAddressId(request.getAddressId());
        orderRequestWithCart.setSystemVoucherCode(request.getSystemVoucherCode());
        orderRequestWithCart.setShopVoucherCode(request.getShopVoucherCode());
        orderRequestWithCart.setPaymentMethod(request.getPaymentMethod());
        orderRequestWithCart.setShippingMethod(request.getShippingMethod());
        orderRequestWithCart.setNote(request.getNote());

        return orderRequestWithCart;
    }
}
