package hcmute.kltn.vtv.service.user;

import hcmute.kltn.vtv.model.data.user.response.OrderItemResponse;
import hcmute.kltn.vtv.model.entity.user.Customer;
import hcmute.kltn.vtv.model.entity.user.Order;
import hcmute.kltn.vtv.model.entity.user.OrderItem;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IOrderItemService {
    OrderItem getOrderItemById(UUID orderItemId);

    OrderItemResponse getOrderItemByOrderItemId(UUID orderItemId);

    @Transactional
    List<OrderItem> addNewOrderItemsByCartIds(Order order, List<UUID> cartIds, String username);

    @Transactional
    List<OrderItem> addNewOrderItemsByyMapProductVariant(Order order, Map<Long, Integer> productVariantsAndQuantities, String username);

    List<OrderItem> createOrderItemsByCartIds(String username, List<UUID> cartIds);

    List<OrderItem> createOrderItemsByMapProductVariantIdsAndQuantities(Customer customer, Map<Long, Integer> mapProductVariantIdsAndQuantities);



    @Transactional
    List<OrderItem> cancelOrderItem(Order order);

    void checkExistOrderItem(UUID orderItemId);

    void checkExistOrderItemByIdAndUsername(UUID orderItemId, String username);

    void checkOrderCompletedBeforeReview(UUID orderItemId);
}
