package hcmute.kltn.vtv.service.user.impl;

import hcmute.kltn.vtv.model.entity.user.*;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.user.request.CreateOrderUpdateRequest;
import hcmute.kltn.vtv.model.data.user.response.ListOrderResponse;
import hcmute.kltn.vtv.model.data.user.response.OrderResponse;
import hcmute.kltn.vtv.model.dto.user.OrderDTO;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.user.OrderRepository;
import hcmute.kltn.vtv.service.user.*;
import hcmute.kltn.vtv.service.vtv.shippingstrategy.GiaoHangHoaTocShippingStrategy;
import hcmute.kltn.vtv.service.vtv.shippingstrategy.GiaoHangNhanhShippingStrategy;
import hcmute.kltn.vtv.service.vtv.shippingstrategy.GiaoHangTietKiemShippingStrategy;
import hcmute.kltn.vtv.service.vtv.shippingstrategy.IShippingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

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

    @Override
    public OrderResponse createOrder(String username, List<Long> cartIds) {
        Order order = createTemporaryOrder(username, cartIds);
        return orderResponse(username, order, "Tạo đơn hàng mới thành công.", true);
    }

    @Override
    public OrderResponse createOrderUpdate(CreateOrderUpdateRequest request) {
        Order order = createTemporaryOrderUpdate(request);

        return orderResponse(request.getUsername(), order, "Cập nhật đơn hàng thành công.", false);
    }

    @Transactional
    @Override
    public OrderResponse saveOrder(CreateOrderUpdateRequest request) {
        Order order = createTemporaryOrderUpdate(request);

        // Sau này nếu có nhiều hình thước thanh toán sẻ sử lý ở đây.
        switch (order.getPaymentMethod()) {
            case "COD":
                order.setStatus(Status.PENDING);
                break;
            default:
                order.setStatus(Status.PENDING);
                break;
        }

        order.setCreateAt(LocalDateTime.now());
        order.setUpdateAt(LocalDateTime.now());

        try {
            Order save = orderRepository.save(order);

            if (request.getVoucherShopId() != null) {
                VoucherOrder voucherOrder = voucherOrderService.saveVoucherOrder(request.getVoucherShopId(), save,
                        true);
                // voucherOrder.setOrder(save);
                save.setVoucherOrders(List.of(voucherOrder));
            }

            if (request.getVoucherSystemId() != null) {
                VoucherOrder voucherOrder = voucherOrderService.saveVoucherOrder(request.getVoucherSystemId(), save,
                        false);
                if (save.getVoucherOrders() != null) {
                    List<VoucherOrder> voucherOrders = new ArrayList<>(save.getVoucherOrders());
                    voucherOrders.add(voucherOrder);

                    save.setVoucherOrders(voucherOrders);
                } else {
                    save.setVoucherOrders(List.of(voucherOrder));
                }
            }

            List<OrderItem> orderItems = orderItemService.saveOrderItem(save);
            save.setOrderItems(orderItems);

            return orderResponse(request.getUsername(), save, "Đặt hàng thành công.", false);
        } catch (Exception e) {
            throw new BadRequestException("Đặt hàng thất bại! " + e.getMessage() + " " + e.getCause());
        }
    }

    @Override
    public ListOrderResponse getOrders(String username) {
        List<Order> orders = orderRepository.findAllByCustomerUsername(username)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy đơn hàng!"));
        return listOrderResponse(orders, "Lấy danh sách đơn hàng thành công.", username);
    }

    @Override
    public ListOrderResponse getOrdersByStatus(String username, Status status) {
        List<Order> orders = orderRepository.findAllByCustomerUsernameAndStatus(username, status)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy đơn hàng!"));

        String message = messageByOrderStatus(status);

        return listOrderResponse(orders, message, username);
    }

    @Override
    public OrderResponse getOrderDetail(String username, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy đơn hàng!"));
        if (!order.getCustomer().getUsername().equals(username)) {
            throw new BadRequestException("Không tìm thấy đơn hàng!");
        }
        return orderResponse(username, order, "Lấy chi tiết đơn hàng thành công.", true);
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(String username, Long orderId) {
        Order order = orderRepository.findByOrderIdAndStatus(orderId, Status.PENDING)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy đơn hàng!"));
        if (order == null || !order.getCustomer().getUsername().equals(username)) {
            throw new BadRequestException("Không tìm thấy đơn hàng!");
        }

        order.setStatus(Status.CANCEL);
        order.setUpdateAt(LocalDateTime.now());
        try {
            Order save = orderRepository.save(order);

            if (save.getVoucherOrders() != null) {
                for (VoucherOrder voucherOrder : save.getVoucherOrders()) {
                    voucherOrderService.cancelVoucherOrder(voucherOrder.getVoucherOrderId());
                }
            }

            List<OrderItem> orderItems = orderItemService.cancelOrderItem(save);
            save.setOrderItems(orderItems);

            return orderResponse(username, save, "Hủy đơn hàng thành công.", false);
        } catch (Exception e) {
            throw new BadRequestException("Hủy đơn hàng thất bại!");
        }
    }

    @Override
    public String messageByOrderStatus(Status status) {
        String message;
        switch (status) {
            case PENDING:
                message = "Lấy danh sách đơn hàng đang chờ xử lý thành công.";
                break;
            case PROCESSING:
                message = "Lấy danh sách đơn hàng đang xử lý thành công.";
                break;
            case SHIPPING:
                message = "Lấy danh sách đơn hàng đang giao thành công.";
                break;
            case COMPLETED:
                message = "Lấy danh sách đơn hàng đã giao thành công.";
                break;
            case CANCEL:
                message = "Lấy danh sách đơn hàng đã hủy thành công.";
                break;
            case CART:
                message = "Lấy danh sách giỏ hàng thành công.";
                break;
            default:
                message = "Lấy danh sách đơn hàng thành công.";
                break;
        }

        return message;
    }

    private Order createTemporaryOrderUpdate(CreateOrderUpdateRequest request) {
        Order order = createTemporaryOrder(request.getUsername(), request.getCartIds());

        if (request.getAddressId() != null && request.getAddressId().equals(order.getAddress().getAddressId())) {
            order.setAddress(addressService.checkAddress(request.getAddressId(), request.getUsername()));
        }

        // order.setPaymentMethod(request.getPaymentMethod());

        if (request.getShippingMethod().equals(order.getShippingMethod())) {
            order.setShippingMethod(request.getShippingMethod());
            order.setShippingFee(calculateShippingFee(order.getShippingMethod(), order.getTotalPrice()));
        }

        if (!request.getNote().isEmpty()) {
            order.setNote(request.getNote());
        }

        if (request.getVoucherShopId() != null) {
            Long discount = voucherOrderService.calculateVoucher(request.getVoucherShopId(), order.getShopId(),
                    order.getTotalPrice(), true);
            order.setDiscount(order.getDiscount() + discount);
        }

        if (request.getVoucherSystemId() != null) {
            Long discount = voucherOrderService.calculateVoucher(request.getVoucherSystemId(), null,
                    order.getTotalPrice(), false);
            order.setDiscount(order.getDiscount() + discount);
        }

        order.setPaymentTotal(order.getTotalPrice() + order.getShippingFee() - order.getDiscount());
        order.setTotalPrice(order.getTotalPrice());

        return order;
    }

    private Long calculateShippingFee(String shippingMethod, Long totalPrice) {
        IShippingStrategy shippingStrategy = null;
        if (shippingMethod.equals("GHTK")) {
            shippingStrategy = new GiaoHangTietKiemShippingStrategy();
        } else {
            shippingStrategy = new GiaoHangHoaTocShippingStrategy();
        }
        return shippingStrategy.calculateShippingCost(totalPrice);
    }

    private Order createTemporaryOrder(String username, List<Long> cartIds) {

        Customer customer = customerService.getCustomerByUsername(username);

        checkListCartSameShop(username, cartIds);

        List<OrderItem> orderItems = orderItemService.createOrderItems(username, cartIds);
        Address address = addressService.getAddressActiveByUsername(username);

        Long totalPrice = getTotalPrice(orderItems);
        Long discount = 0L;

        IShippingStrategy shippingStrategy = new GiaoHangNhanhShippingStrategy();
        Long shippingFee = shippingStrategy.calculateShippingCost(totalPrice);

        Long totalPayment = totalPrice + shippingFee - discount;
        Long shopId = orderItems.get(0).getCart().getProductVariant().getProduct().getCategory().getShop().getShopId();
        String shopName = orderItems.get(0).getCart().getProductVariant().getProduct().getCategory().getShop()
                .getName();

        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderItems(orderItems);
        order.setAddress(address);
        order.setStatus(Status.CART);
        order.setTotalPrice(totalPrice);
        order.setVoucherOrders(null);
        order.setPaymentMethod("COD");
        order.setShippingMethod("GHN");
        order.setPaymentTotal(totalPayment);
        order.setDiscount(discount);
        order.setNote(null);
        order.setOrderDate(new Date());
        order.setOrderItems(orderItems);
        order.setCount(getTotalCount(orderItems));
        order.setShopId(shopId);
        order.setShopName(shopName);
        order.setShippingFee(shippingFee);
        return order;
    }

//    private Order createOrder(Customer customer, )

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

    private Long getTotalPrice(List<OrderItem> orderItems) {
        long totalPrice = 0L;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getCart().getProductVariant().getPrice() * orderItem.getCart().getQuantity();
        }
        return totalPrice;
    }

    private int getTotalCount(List<OrderItem> orderItems) {
        int count = 0;
        for (OrderItem orderItem : orderItems) {
            count += orderItem.getCart().getQuantity();
        }
        return count;
    }

    private void checkListCartSameShop(String username, List<Long> cartIds) {
        boolean check = cartService.checkCartsSameShop(username, cartIds);
        if (!check) {
            throw new BadRequestException("Các sản phẩm không thuộc cùng một cửa hàng.");
        }
    }

}
