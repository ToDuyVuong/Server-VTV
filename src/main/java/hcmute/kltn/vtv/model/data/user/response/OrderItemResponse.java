package hcmute.kltn.vtv.model.data.user.response;

import hcmute.kltn.vtv.model.entity.user.OrderItem;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse extends ResponseAbstract {

    private UUID orderItemId;
    private UUID orderId;
    private UUID cartId;
    private Long productVariantId;
    private String sku;
    private String productVariantImage;
    private Long price;
    private int quantity;
    private Long productId;
    private String productName;
    private String productImage;
    private Long shopId;
    private String shopName;


    public static OrderItemResponse orderItemResponse(OrderItem orderItem, String message, String status){
        OrderItemResponse response = new OrderItemResponse();
        response.setOrderItemId(orderItem.getOrderItemId());
        response.setOrderId(orderItem.getOrder().getOrderId());
        response.setCartId(orderItem.getCart().getCartId());
        response.setProductVariantId(orderItem.getCart().getProductVariant().getProductVariantId());
        response.setSku(orderItem.getCart().getProductVariant().getSku());
        response.setProductVariantImage(orderItem.getCart().getProductVariant().getImage());
        response.setQuantity(orderItem.getCart().getQuantity());
        response.setPrice(orderItem.getCart().getProductVariant().getPrice());
        response.setProductId(orderItem.getCart().getProductVariant().getProduct().getProductId());
        response.setProductName(orderItem.getCart().getProductVariant().getProduct().getName());
        response.setProductImage(orderItem.getCart().getProductVariant().getProduct().getImage());
        response.setShopId(orderItem.getCart().getProductVariant().getProduct().getShop().getShopId());
        response.setShopName(orderItem.getCart().getProductVariant().getProduct().getShop().getName());
        response.setCode(200);
        response.setMessage(message);
        response.setStatus(status);

        return response;
    }
}
