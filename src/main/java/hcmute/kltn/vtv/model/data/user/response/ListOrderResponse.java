package hcmute.kltn.vtv.model.data.user.response;

import hcmute.kltn.vtv.model.dto.user.OrderDTO;
import hcmute.kltn.vtv.model.entity.user.Order;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ListOrderResponse extends ResponseAbstract {

    private int count;

    private List<OrderDTO> orderDTOs;


    public static ListOrderResponse listOrderResponse(List<Order> orders, String message, String status) {
        ListOrderResponse response = new ListOrderResponse();
        response.setOrderDTOs(OrderDTO.convertEntitiesToDTOs(orders));
        response.setMessage(message);
        response.setStatus(status);
        response.setCode(200);
        response.setCount(orders.size());
        return response;
    }

}
