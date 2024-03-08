package hcmute.kltn.vtv.service.vendor.impl;

import hcmute.kltn.vtv.model.extra.OrderStatus;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.paging.response.PageOrderResponse;
import hcmute.kltn.vtv.model.data.user.response.ListOrderResponse;
import hcmute.kltn.vtv.model.data.user.response.OrderResponse;
import hcmute.kltn.vtv.model.dto.user.OrderDTO;
import hcmute.kltn.vtv.model.dto.vtv.ShopDTO;
import hcmute.kltn.vtv.model.entity.user.Order;
import hcmute.kltn.vtv.model.entity.user.OrderItem;
import hcmute.kltn.vtv.model.entity.vtv.Shop;
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
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderShopServiceImpl implements IOrderShopService {

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
    @Autowired
    private final IOrderItemShopService orderItemShopService;

    @Override
    public PageOrderResponse getPageOrder(String username, int page, int size) {
        Shop shop = shopService.getShopByUsername(username);

        int totalOrder = orderRepository.countAllByShopId(shop.getShopId());
        int totalPage = (int) Math.ceil((double) totalOrder / size);

        Page<Order> pageOrder = orderRepository.findAllByShopIdOrderByCreateAtDesc(shop.getShopId(),
                PageRequest.of(page - 1, size))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng nào!"));

        return getPageOrderResponse(pageOrder.getContent(), shop, page, size, totalPage,
                "Lấy danh sách đơn hàng thành công!");

    }

    @Override
    public PageOrderResponse getPageOrderByStatus(String username, OrderStatus status, int page, int size) {
        Shop shop = shopService.getShopByUsername(username);

        int totalOrder = orderRepository.countAllByShopIdAndStatus(shop.getShopId(), status);
        int totalPage = (int) Math.ceil((double) totalOrder / size);

        Page<Order> pageOrder = orderRepository.findAllByShopIdAndStatusOrderByCreateAtDesc(shop.getShopId(), status,
                PageRequest.of(page - 1, size))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng nào!"));

        return getPageOrderResponse(pageOrder.getContent(), shop, page, size, totalPage,
                "Lấy danh sách đơn hàng theo trạng thái thành công!");

    }

    public PageOrderResponse getPageOrderResponse(List<Order> orders, Shop shop, int page,
            int size, int totalPage, String message) {

        PageOrderResponse response = new PageOrderResponse();
        response.setPage(page);
        response.setSize(size);
        response.setTotalPage(totalPage);
        response.setCount(orders.size());
        response.setOrderDTOs(OrderDTO.convertListEntityToDTOs(orders));
        response.setShopDTO(ShopDTO.convertEntityToDTO(shop));
        response.setMessage(message);
        response.setStatus("ok");
        response.setCode(200);
        return response;
    }

    @Override
    public ListOrderResponse getOrders(String username) {
        Shop shop = shopService.getShopByUsername(username);

        List<Order> orders = orderRepository.findAllByShopId(shop.getShopId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng nào!"));

        return listOrderResponse(orders, "Lấy danh sách đơn hàng thành công!", username);
    }

    @Override
    public ListOrderResponse getOrdersByStatus(String username, OrderStatus status) {
        Shop shop = shopService.getShopByUsername(username);

        List<Order> orders = orderRepository.findAllByShopIdAndStatus(shop.getShopId(), status)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng nào!"));

        String message = orderService.messageByOrderStatus(status);

        return listOrderResponse(orders, message, username);
    }

    @Override
    public ListOrderResponse getOrdersOnSameDay(String username, Date orderDate) {
        Shop shop = shopService.getShopByUsername(username);

        Date startOfDay = startOfDay(orderDate);
        Date endOfDay = endOfDay(orderDate);

        List<Order> orders = orderRepository.findAllByShopIdAndOrderDateBetween(shop.getShopId(), startOfDay, endOfDay)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng nào!"));

        return listOrderResponse(orders, "Lấy danh sách đơn hàng trong cùng ngày thành công.", username);
    }

    @Override
    public ListOrderResponse getOrdersOnSameDayByStatus(String username, Date orderDate, OrderStatus status) {
        Shop shop = shopService.getShopByUsername(username);

        Date startOfDay = startOfDay(orderDate);
        Date endOfDay = endOfDay(orderDate);

        List<Order> orders = orderRepository
                .findAllByShopIdAndOrderDateBetweenAndStatus(shop.getShopId(), startOfDay, endOfDay, status)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng nào!"));

        String message = orderService.messageByOrderStatus(status);

        return listOrderResponse(orders, message, username);
    }

    @Override
    public ListOrderResponse getOrdersBetweenDate(String username, Date startOrderDate, Date endOrderDate) {
        Shop shop = shopService.getShopByUsername(username);

        Date startOfDay = startOfDay(startOrderDate);
        Date endOfDay = endOfDay(endOrderDate);

        List<Order> orders = orderRepository.findAllByShopIdAndOrderDateBetween(shop.getShopId(), startOfDay, endOfDay)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng nào!"));

        return listOrderResponse(orders, "Lấy danh sách đơn hàng trong khoảng thời gian thành công.", username);
    }

    @Override
    public ListOrderResponse getOrdersBetweenDateByStatus(String username, Date startOrderDate, Date endOrderDate,
                                                          OrderStatus status) {
        Shop shop = shopService.getShopByUsername(username);

        Date startOfDay = startOfDay(startOrderDate);
        Date endOfDay = endOfDay(endOrderDate);

        List<Order> orders = orderRepository
                .findAllByShopIdAndOrderDateBetweenAndStatus(shop.getShopId(), startOfDay, endOfDay, status)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng nào!"));

        String message = orderService.messageByOrderStatus(status);

        return listOrderResponse(orders, message, username);
    }

    @Override
    public OrderResponse getOrderById(String username, UUID orderId) {
        Shop shop = shopService.getShopByUsername(username);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng nào!"));

        if (!order.getShopId().equals(shop.getShopId())) {
            throw new NotFoundException("Không tìm thấy đơn hàng nào!");
        }

        return orderResponse(username, order, "Lấy đơn hàng thành công!", true);
    }

    @Override
    public OrderResponse updateStatusOrder(String username, UUID orderId, OrderStatus status) {
        Shop shop = shopService.getShopByUsername(username);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng nào!"));
        if (!order.getShopId().equals(shop.getShopId())) {
            throw new NotFoundException("Không tìm thấy đơn hàng nào!");
        }
        checkStatus(order, status);

        order.setStatus(status);
        order.setUpdateAt(LocalDateTime.now());

        try {
            Order save = orderRepository.save(order);
            if (status.equals(Status.CANCEL)) {
                if (save.getVoucherOrders() != null) {
                    for (VoucherOrder voucherOrder : save.getVoucherOrders()) {
                        voucherOrderService.cancelVoucherOrder(voucherOrder.getVoucherOrderId());
                    }
                }
                List<OrderItem> orderItems = orderItemService.cancelOrderItem(save);
                save.setOrderItems(orderItems);
            } else {

                List<OrderItem> orderItems = orderItemShopService.updateStatusOrderItemByShop(save, status);
                save.setOrderItems(orderItems);
            }

            String message = messageUpdateStatusOrder(status);

            return orderResponse(username, save, message, false);
        } catch (Exception e) {
            throw new BadRequestException("Cập nhật trạng thái đơn hàng thất bại! " + e.getMessage());
        }

    }

    private String messageUpdateStatusOrder(OrderStatus status) {
        switch (status) {
            case CANCEL:
                return "Hủy đơn hàng thành công!";
            case COMPLETED:
                return "Giao đơn hàng thành công!";
            case RETURNED:
                return "Trả đơn hàng thành công!";
            case REFUNDED:
                return "Hoàn tiền đơn hàng thành công!";
            default:
                return "Cập nhật trạng thái đơn hàng thành công!";
        }
    }

    private void checkStatus(Order order, OrderStatus status) {

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

    private OrderResponse orderResponse(String username, Order order, String message, boolean created) {

        OrderDTO orderDTO = OrderDTO.convertEntityToDTOCreate(order);

        OrderResponse response = new OrderResponse();
        response.setUsername(username);
        response.setOrderDTO(orderDTO);
        response.setMessage(message);
        response.setStatus(created ? "ok" : "success");
        response.setCode(200);
        return response;
    }

    public ListOrderResponse listOrderResponse(List<Order> orders, String message, String username) {
        List<OrderDTO> orderDTOs = OrderDTO.convertListEntityToDTOs(orders);
        ListOrderResponse response = new ListOrderResponse();
        response.setOrderDTOs(orderDTOs);
        response.setUsername(username);
        response.setMessage(message);
        response.setStatus("ok");
        response.setCode(200);
        response.setCount(orders.size());
        return response;
    }

    @Override
    public void checkRequestPageParams(int page, int size) {
        if (page < 0) {
            throw new NotFoundException("Trang không được nhỏ hơn 0!");
        }
        if (size < 0) {
            throw new NotFoundException("Kích thước trang không được nhỏ hơn 0!");
        }
        if (size > 200) {
            throw new NotFoundException("Kích thước trang không được lớn hơn 200!");
        }
    }

}
