package hcmute.kltn.vtv.model.dto.user;

import hcmute.kltn.vtv.model.dto.vendor.ProductVariantDTO;
import hcmute.kltn.vtv.model.entity.user.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class OrderItemDTO {

    private UUID orderItemId;

    private UUID orderId;

    private UUID cartId;

    private int quantity;

    private Long price;

    private Long totalPrice;

    private ProductVariantDTO productVariantDTO;


    public static OrderItemDTO convertEntityToDTO(OrderItem orderItem) {
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setProductVariantDTO(ProductVariantDTO.convertEntityToDTO(orderItem.getCart().getProductVariant()));
        orderItemDTO.setOrderItemId(orderItem.getOrderItemId());
        orderItemDTO.setOrderId(orderItem.getOrder().getOrderId());
        orderItemDTO.setCartId(orderItem.getCart().getCartId());
        orderItemDTO.setQuantity(orderItem.getCart().getQuantity());
        orderItemDTO.setPrice(orderItem.getPrice());
        orderItemDTO.setTotalPrice(orderItem.getPrice() * orderItem.getCart().getQuantity());

        return orderItemDTO;
    }

    public static List<OrderItemDTO> convertListEntityToListDTO(List<OrderItem> orderItems) {

        if (orderItems == null) {
            return null;
        }
        List<OrderItemDTO> orderItemDTOs = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            OrderItemDTO orderItemDTO = convertEntityToDTO(orderItem);
            orderItemDTOs.add(orderItemDTO);
        }
        return orderItemDTOs;
    }

}
