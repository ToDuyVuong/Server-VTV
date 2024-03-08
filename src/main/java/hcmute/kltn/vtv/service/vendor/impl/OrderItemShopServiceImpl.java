package hcmute.kltn.vtv.service.vendor.impl;

import hcmute.kltn.vtv.model.extra.CartStatus;
import hcmute.kltn.vtv.model.extra.OrderStatus;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.entity.user.Cart;
import hcmute.kltn.vtv.model.entity.user.Order;
import hcmute.kltn.vtv.model.entity.user.OrderItem;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.user.CartRepository;
import hcmute.kltn.vtv.repository.user.OrderItemRepository;
import hcmute.kltn.vtv.repository.user.OrderRepository;
import hcmute.kltn.vtv.service.admin.IVoucherAdminService;
import hcmute.kltn.vtv.service.user.*;
import hcmute.kltn.vtv.service.vendor.IOrderItemShopService;
import hcmute.kltn.vtv.service.vendor.IShopService;
import hcmute.kltn.vtv.service.vendor.IVoucherShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemShopServiceImpl implements IOrderItemShopService {

    @Autowired
    private final OrderRepository orderRepository;
    @Autowired
    private final IOrderItemService orderItemService;
    @Autowired
    private final IVoucherOrderService voucherOrderService;
    @Autowired
    private final ICartService cartService;
    @Autowired
    private final ICustomerService customerService;
    @Autowired
    private final IAddressService addressService;
    @Autowired
    private final IVoucherShopService voucherShopService;
    @Autowired
    private final IVoucherAdminService voucherSystemService;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private final IShopService shopService;
    @Autowired
    private final IOrderService orderService;

    @Transactional
    @Override
    public List<OrderItem> updateStatusOrderItemByShop(Order order, Status status) {
        List<OrderItem> orderItems = new ArrayList<>();
//        for (OrderItem orderItem : order.getOrderItems()) {
//            Cart cart = orderItem.getCart();
//            cart.setStatus(status);
//            cart.setUpdateAt(order.getUpdateAt());
//            try {
//                orderItem.setCart(cartRepository.save(cart));
//                orderItems.add(orderItem);
//            } catch (Exception e) {
//                throw new BadRequestException("Cập nhật trạng thái đơn hàng thất bại!");
//            }
//        }
        return orderItems;
    }
}
