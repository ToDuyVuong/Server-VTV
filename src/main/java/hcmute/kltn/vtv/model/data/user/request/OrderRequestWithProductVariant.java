package hcmute.kltn.vtv.model.data.user.request;


import hcmute.kltn.vtv.model.data.user.BaseOrderRequest;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestWithProductVariant extends BaseOrderRequest {

    private Map<Long, Integer> productVariantIdsAndQuantities;

    public static void validate(OrderRequestWithProductVariant request) {
        request.validate();

        if (request.getProductVariantIdsAndQuantities() == null || request.getProductVariantIdsAndQuantities().isEmpty()) {
            throw new BadRequestException("Danh sách sản phẩm không được để trống!");
        }

        request.getProductVariantIdsAndQuantities().forEach((k, v) -> {
            if (k == null || v == null ) {
                throw new BadRequestException("Danh sách sản phẩm không hợp lệ!");
            }
            if (v <= 0){
                throw new BadRequestException("Số lượng biến thể sản phẩm của mã biến thể sản phẩm " + k + " không hợp lệ!");
            }
        });


    }
}

