package hcmute.kltn.vtv.service.user;

import hcmute.kltn.vtv.model.data.user.response.OrderItemResponse;
import hcmute.kltn.vtv.model.entity.user.Order;
import hcmute.kltn.vtv.model.entity.user.OrderItem;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface IOrderItemService {
    OrderItemResponse getOrderItemByOrderItemId(UUID orderItemId);

    List<OrderItem> createOrderItems(String username, List<UUID> cartIds);

    @Transactional
    List<OrderItem> saveOrderItem(Order order);

    @Transactional
    List<OrderItem> cancelOrderItem(Order order);
}
