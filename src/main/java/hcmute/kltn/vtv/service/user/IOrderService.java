package hcmute.kltn.vtv.service.user;

import hcmute.kltn.vtv.model.data.user.request.CreateOrderUpdateRequest;
import hcmute.kltn.vtv.model.data.user.request.OrderRequestWithCartIds;
import hcmute.kltn.vtv.model.data.user.request.OrderRequestWithProductVariant;
import hcmute.kltn.vtv.model.data.user.response.ListOrderResponse;
import hcmute.kltn.vtv.model.data.user.response.OrderResponse;
import hcmute.kltn.vtv.model.extra.OrderStatus;
import hcmute.kltn.vtv.model.extra.Status;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IOrderService {


    @Transactional
    OrderResponse createOrderByCartIds(String username, List<UUID> cartIds);
    @Transactional
    OrderResponse createOrderByMapProductVariantsAndQuantities(String username, Map<Long, Integer> productVariantIdsAndQuantities);
    @Transactional
    OrderResponse createOrderByOrderRequestWithProductVariant(OrderRequestWithProductVariant request, String username);
    @Transactional
    OrderResponse createOrderByOrderRequestWithCartIds(OrderRequestWithCartIds request, String username);

    @Transactional
    OrderResponse addNewOrderByOrderRequestWithCart(OrderRequestWithCartIds request, String username);

    @Transactional
    OrderResponse createOrderUpdate(CreateOrderUpdateRequest request);

    @Transactional
    OrderResponse saveOrder(CreateOrderUpdateRequest request);

    ListOrderResponse getOrders(String username);

    ListOrderResponse getOrdersByStatus(String username, OrderStatus status);

    OrderResponse getOrderDetail(String username, UUID orderId);

    @Transactional
    OrderResponse cancelOrder(String username, UUID orderId);

    String messageByOrderStatus(OrderStatus status);


}
