package hcmute.kltn.vtv.service.user;

import hcmute.kltn.vtv.model.data.user.request.CreateOrderUpdateRequest;
import hcmute.kltn.vtv.model.data.user.response.ListOrderResponse;
import hcmute.kltn.vtv.model.data.user.response.OrderResponse;
import hcmute.kltn.vtv.model.extra.OrderStatus;
import hcmute.kltn.vtv.model.extra.Status;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IOrderService {
    OrderResponse createOrder(String username, List<Long> cartIds);

    OrderResponse createOrderUpdate(CreateOrderUpdateRequest request);

    @Transactional
    OrderResponse saveOrder(CreateOrderUpdateRequest request);

    ListOrderResponse getOrders(String username);

    ListOrderResponse getOrdersByStatus(String username, OrderStatus status);

    OrderResponse getOrderDetail(String username, Long orderId);

    @Transactional
    OrderResponse cancelOrder(String username, Long orderId);

    String messageByOrderStatus(OrderStatus status);
}
