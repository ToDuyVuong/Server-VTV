package hcmute.kltn.vtv.service.vendor.impl;

import hcmute.kltn.vtv.model.dto.shipping.ShippingDTO;
import hcmute.kltn.vtv.model.extra.OrderStatus;
import hcmute.kltn.vtv.service.shipping.IShippingService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.paging.response.PageOrderResponse;
import hcmute.kltn.vtv.model.data.user.response.ListOrderResponse;
import hcmute.kltn.vtv.model.data.user.response.OrderResponse;
import hcmute.kltn.vtv.model.dto.user.OrderDTO;
import hcmute.kltn.vtv.model.dto.vtv.ShopDTO;
import hcmute.kltn.vtv.model.entity.user.Order;
import hcmute.kltn.vtv.model.entity.user.OrderItem;
import hcmute.kltn.vtv.model.entity.vendor.Shop;
import hcmute.kltn.vtv.model.entity.user.VoucherOrder;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.user.CartRepository;
import hcmute.kltn.vtv.repository.user.OrderItemRepository;
import hcmute.kltn.vtv.repository.user.OrderRepository;
import hcmute.kltn.vtv.service.admin.IVoucherAdminService;
import hcmute.kltn.vtv.service.user.*;
import hcmute.kltn.vtv.service.vendor.IOrderItemShopService;
import hcmute.kltn.vtv.service.vendor.IOrderShopService;
import hcmute.kltn.vtv.service.vendor.IShopService;
import hcmute.kltn.vtv.service.vendor.IVoucherShopService;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderShopServiceImpl implements IOrderShopService {

    private final OrderRepository orderRepository;
    private final IOrderItemService orderItemService;
    private final IVoucherOrderService voucherOrderService;
    private final IShopService shopService;
    private final IOrderService orderService;
    private final IMailService mailService;
    private final IShippingService shippingService;

    @Override
    public PageOrderResponse getPageOrder(String username, int page, int size) {
        Shop shop = shopService.getShopByUsername(username);
        Page<Order> pageOrder = orderRepository.findAllByShopShopIdOrderByCreateAtDesc(shop.getShopId(),
                        PageRequest.of(page - 1, size))
                .orElseThrow(() -> new NotFoundException("Không tìm danh sách đơn hàng nào trong cửa hàng sắp xếp theo thời gian mới nhất!"));

        return PageOrderResponse.pageOrderResponse(pageOrder, "Lấy danh sách đơn hàng thành công!", "OK");
    }


    @Override
    public PageOrderResponse getPageOrderByStatus(String username, OrderStatus status, int page, int size) {
        Shop shop = shopService.getShopByUsername(username);
        Page<Order> pageOrder = orderRepository.findAllByShopShopIdAndStatusOrderByCreateAtDesc(shop.getShopId(), status,
                        PageRequest.of(page - 1, size))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng nào trong cửa hàng sắp xếp theo thời gian mới nhất và trạng thái!"));
        String message = orderService.messageByOrderStatus(status);

        return PageOrderResponse.pageOrderResponse(pageOrder, message, "OK");
    }


    @Override
    public ListOrderResponse getOrders(String username) {
        Shop shop = shopService.getShopByUsername(username);
        List<Order> orders = orderRepository.findAllByShopShopId(shop.getShopId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng nào!"));

        return ListOrderResponse.listOrderResponse(orders, "Lấy danh sách đơn hàng thành công!", "OK");
    }

    @Override
    public ListOrderResponse getOrdersByStatus(String username, OrderStatus status) {
        Shop shop = shopService.getShopByUsername(username);

        List<Order> orders = orderRepository.findAllByShopShopIdAndStatus(shop.getShopId(), status)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng nào!"));

        String message = orderService.messageByOrderStatus(status);

        return ListOrderResponse.listOrderResponse(orders, message, "OK");
    }

    @Override
    public ListOrderResponse getOrdersOnSameDay(String username, Date orderDate) {
        Shop shop = shopService.getShopByUsername(username);
        Date startOfDay = startOfDay(orderDate);
        Date endOfDay = endOfDay(orderDate);
        List<Order> orders = orderRepository.findAllByShopShopIdAndOrderDateBetween(shop.getShopId(), startOfDay, endOfDay)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng nào!"));

        return ListOrderResponse.listOrderResponse(orders, "Lấy danh sách đơn hàng trong cùng ngày thành công.", "OK");
    }

    @Override
    public ListOrderResponse getOrdersOnSameDayByStatus(String username, Date orderDate, OrderStatus status) {
        Shop shop = shopService.getShopByUsername(username);
        Date startOfDay = startOfDay(orderDate);
        Date endOfDay = endOfDay(orderDate);
        List<Order> orders = orderRepository
                .findAllByShopShopIdAndOrderDateBetweenAndStatus(shop.getShopId(), startOfDay, endOfDay, status)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng nào!"));
        String message = orderService.messageByOrderStatus(status);

        return ListOrderResponse.listOrderResponse(orders, message, "OK");
    }

    @Override
    public ListOrderResponse getOrdersBetweenDate(String username, Date startOrderDate, Date endOrderDate) {
        Shop shop = shopService.getShopByUsername(username);
        Date startOfDay = startOfDay(startOrderDate);
        Date endOfDay = endOfDay(endOrderDate);
        List<Order> orders = orderRepository.findAllByShopShopIdAndOrderDateBetween(shop.getShopId(), startOfDay, endOfDay)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng nào!"));

        return ListOrderResponse.listOrderResponse(orders, "Lấy danh sách đơn hàng trong khoảng thời gian thành công.", "OK");
    }

    @Override
    public ListOrderResponse getOrdersBetweenDateByStatus(String username, Date startOrderDate, Date endOrderDate,
                                                          OrderStatus status) {
        Shop shop = shopService.getShopByUsername(username);
        Date startOfDay = startOfDay(startOrderDate);
        Date endOfDay = endOfDay(endOrderDate);
        List<Order> orders = orderRepository
                .findAllByShopShopIdAndOrderDateBetweenAndStatus(shop.getShopId(), startOfDay, endOfDay, status)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng nào!"));

        String message = orderService.messageByOrderStatus(status);

        return ListOrderResponse.listOrderResponse(orders, message, "OK");
    }

    @Override
    public OrderResponse getOrderById(String username, UUID orderId) {
        Shop shop = shopService.getShopByUsername(username);
        Order order = orderRepository.findByOrderIdAndShopShopId(orderId, shop.getShopId())
                .orElseThrow(() -> new NotFoundException("Ma đơn hàng không có trong danh sách đơn hàng của cửa hàng!"));
        ShippingDTO shippingDTO = shippingService.getCalculateShippingByWardAndTransportProvider(order.getAddress().getWard().getWardCode(),
                shop.getWard().getWardCode(), order.getShippingMethod()).getShippingDTO();

        return OrderResponse.orderResponse(order, shippingDTO, "Lấy đơn hàng thành công!", "OK");

    }

    @Override
    public OrderResponse updateStatusOrder(String username, UUID orderId, OrderStatus status) {
        Shop shop = shopService.getShopByUsername(username);
        checkExistOrderById(orderId);
        checkExistOrderByShop(orderId, shop.getShopId());
        Order order = orderRepository.findByOrderIdAndShopShopId(orderId, shop.getShopId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng trong cửa hàng!"));
        checkStatus(order);
        String message = "Cập nhật trạng " + messageUpdateStatusOrder(status) + " cho đơn hàng thành công!";
        ShippingDTO shippingDTO = shippingService.getCalculateShippingByWardAndTransportProvider(order.getAddress().getWard().getWardCode(),
                shop.getWard().getWardCode(), order.getShippingMethod()).getShippingDTO();
        if (status.equals(OrderStatus.CANCEL)) {
            return cancelOrderByShop(order, shippingDTO, message);
        }

        order.setStatus(status);
        order.setUpdateAt(LocalDateTime.now());

        try {
            orderRepository.save(order);

            return OrderResponse.orderResponse(order, shippingDTO, message, "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Cập nhật trạng thái đơn hàng thất bại! " + e.getMessage());
        }
    }


    @Transactional
    public OrderResponse cancelOrderByShop(Order order, ShippingDTO shippingDTO, String message) {

        order.setStatus(OrderStatus.CANCEL);
        order.setUpdateAt(LocalDateTime.now());
        try {
            orderRepository.save(order);

            if (order.getVoucherOrders() != null) {
                for (VoucherOrder voucherOrder : order.getVoucherOrders()) {
                    voucherOrderService.cancelVoucherOrder(voucherOrder.getVoucherOrderId());
                }
            }

            List<OrderItem> orderItems = orderItemService.cancelOrderItem(order);
            order.setOrderItems(orderItems);

            String messageMail = "Đơn hàng của bạn đã bị hủy bởi cửa hàng!";
            mailService.sendOrderConfirmationEmail(order, shippingDTO, messageMail);

            return OrderResponse.orderResponse(order, message, "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Hủy đơn hàng từ cửa hàng thất bại!");
        }
    }


    private void checkExistOrderById(UUID orderId) {
        if (!orderRepository.existsByOrderId(orderId)) {
            throw new NotFoundException("Mã đơn hàng không tồn tại!");
        }
    }

    private void checkExistOrderByShop(UUID orderId, Long shopId) {
        if (!orderRepository.existsByOrderIdAndShopShopId(orderId, shopId)) {
            throw new NotFoundException("Mã đơn hàng không tồn tại trong cửa hàng!");
        }
    }

    private String messageUpdateStatusOrder(OrderStatus status) {
        return switch (status) {
            case CANCEL -> "hủy";
            case PENDING -> "chờ xác nhận";
            case PROCESSING -> "xác nhận";
            case PICKUP_PENDING -> "chờ đơn vị vận chuyển lấy hàng";
            case WAITING -> "chờ xử lý";
            default -> "";
        };
    }

    private void checkStatus(Order order) {

        if (order.getStatus().equals(OrderStatus.CANCEL)) {
            throw new BadRequestException("Đơn hàng đã bị hủy!");
        }

        if (order.getStatus().equals(OrderStatus.COMPLETED)) {
            throw new BadRequestException("Đơn hàng đã được giao!");
        }

        if (order.getStatus().equals(OrderStatus.RETURNED)) {
            throw new BadRequestException("Đơn hàng đã được trả!");
        }

        if (order.getStatus().equals(OrderStatus.REFUNDED)) {
            throw new BadRequestException("Đơn hàng đã được hoàn tiền!");
        }

    }

    private Date startOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private Date endOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }


//    @Override
//    public void checkRequestPageParams(int page, int size) {
//        if (page < 0) {
//            throw new NotFoundException("Trang không được nhỏ hơn 0!");
//        }
//        if (size < 0) {
//            throw new NotFoundException("Kích thước trang không được nhỏ hơn 0!");
//        }
//        if (size > 200) {
//            throw new NotFoundException("Kích thước trang không được lớn hơn 200!");
//        }
//    }

}
