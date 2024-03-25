package hcmute.kltn.vtv.model.data.user.response;

import hcmute.kltn.vtv.model.dto.shipping.ShippingDTO;
import hcmute.kltn.vtv.model.dto.user.OrderDTO;
import hcmute.kltn.vtv.model.entity.user.Order;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse extends ResponseAbstract {

    private OrderDTO orderDTO;

    private ShippingDTO shippingDTO;


    public static OrderResponse orderResponse(Order order, String message, String status) {
        OrderResponse response = new OrderResponse();
        response.setOrderDTO(OrderDTO.convertEntityToDTO(order));
        response.setMessage(message);
        response.setStatus(status);
        response.setCode(200);

        return response;
    }

    public static OrderResponse orderResponse(Order order, ShippingDTO shippingDTO, String message, String status) {
        OrderResponse response = new OrderResponse();
        response.setOrderDTO(OrderDTO.convertEntityToDTO(order));
        response.setShippingDTO(shippingDTO);
        response.setMessage(message);
        response.setStatus(status);
        response.setCode(200);

        return response;
    }




}
