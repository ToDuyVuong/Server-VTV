package hcmute.kltn.vtv.repository.user;

import hcmute.kltn.vtv.model.entity.user.OrderItem;
import hcmute.kltn.vtv.model.extra.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {

    boolean existsByOrderItemIdAndCartCustomerUsername(UUID orderItemId, String username);

    boolean existsByOrderItemIdAndOrderStatus(UUID orderItemId, OrderStatus orderStatus);

}
