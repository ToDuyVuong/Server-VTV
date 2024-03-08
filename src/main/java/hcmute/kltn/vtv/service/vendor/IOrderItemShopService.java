package hcmute.kltn.vtv.service.vendor;

import hcmute.kltn.vtv.model.entity.user.Order;
import hcmute.kltn.vtv.model.entity.user.OrderItem;
import hcmute.kltn.vtv.model.extra.CartStatus;
import hcmute.kltn.vtv.model.extra.OrderStatus;
import hcmute.kltn.vtv.model.extra.Status;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IOrderItemShopService {
    @Transactional
    List<OrderItem> updateStatusOrderItemByShop(Order order, OrderStatus status);
}
