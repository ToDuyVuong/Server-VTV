package hcmute.kltn.vtv.model.data.user.response;

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
}
