package hcmute.kltn.vtv.model.data.paging.response;

import hcmute.kltn.vtv.model.dto.user.OrderDTO;
import hcmute.kltn.vtv.model.dto.vtv.ShopDTO;
import hcmute.kltn.vtv.model.entity.user.Order;
import hcmute.kltn.vtv.model.entity.vendor.Shop;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PageOrderResponse extends ResponseAbstract {

    private int count;
    private int page;
    private int size;
    private int totalPage;
    private List<OrderDTO> orderDTOs;

    public static PageOrderResponse pageOrderResponse(Page<Order> orders, String message, String status) {
        PageOrderResponse response = new PageOrderResponse();
        response.setOrderDTOs(OrderDTO.convertEntitiesToDTOs(orders.getContent()));
        response.setMessage(message);
        response.setStatus(status);
        response.setCode(200);
        response.setCount(orders.getNumberOfElements());
        response.setPage(orders.getNumber() + 1);
        response.setSize(orders.getSize());
        response.setTotalPage(orders.getTotalPages());

        return response;
    }
}
