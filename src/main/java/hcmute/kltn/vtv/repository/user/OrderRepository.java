package hcmute.kltn.vtv.repository.user;

import hcmute.kltn.vtv.model.entity.user.Order;
import hcmute.kltn.vtv.model.extra.OrderStatus;
import hcmute.kltn.vtv.model.extra.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    // @Query("SELECT SUM(o.totalMoney) FROM Order o WHERE o.shopId = :shopId AND
    // o.status = :status AND o.orderDate BETWEEN :startDate AND :endDate")
    // Long sumPaymentTotalByShopIdAndStatusAndOrderDateBetween(
    // @Param("shopId") Long shopId,
    // @Param("status") Status status,
    // @Param("startDate") Date startDate,
    // @Param("endDate") Date endDate
    // );

    Optional<List<Order>> findAllByCustomerUsername(String username);

    Optional<List<Order>> findAllByCustomerUsernameAndStatus(String username, OrderStatus status);

    Optional<Order> findByOrderIdAndStatus(UUID orderId, OrderStatus status);

    Optional<List<Order>> findAllByShopId(Long shopId);

    Optional<List<Order>> findAllByShopIdAndStatus(Long shopId, OrderStatus status);

    Optional<List<Order>> findAllByShopIdAndOrderDateBetween(Long shopId, Date startOrderDate, Date endOrderDate);

    Optional<List<Order>> findAllByShopIdAndOrderDateBetweenAndStatus(Long shopId, Date startOrderDate,
            Date endOrderDate, OrderStatus status);

    int countAllByShopIdAndStatusAndOrderDateBetween(Long shopId, OrderStatus status, Date startDate, Date endDate);

    Optional<List<Order>> findAllByShopIdAndStatusAndOrderDateBetween(Long shopId, OrderStatus status, Date startDate,
            Date endDate);

    Optional<Page<Order>> findAllByShopIdOrderByCreateAtDesc(Long shopId, PageRequest pageRequest);

    Optional<Page<Order>> findAllByShopIdAndStatusOrderByCreateAtDesc(Long shopId, OrderStatus status,
            PageRequest pageRequest);

    int countAllByShopIdAndStatus(Long shopId, OrderStatus status);

    int countAllByShopId(Long shopId);

}
