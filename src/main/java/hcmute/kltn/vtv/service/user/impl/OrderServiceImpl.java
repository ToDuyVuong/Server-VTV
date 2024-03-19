package hcmute.kltn.vtv.service.user.impl;

import hcmute.kltn.vtv.model.entity.user.*;
import hcmute.kltn.vtv.model.entity.vendor.ProductVariant;
import hcmute.kltn.vtv.model.entity.vendor.Shop;
import hcmute.kltn.vtv.model.extra.OrderStatus;
import hcmute.kltn.vtv.service.guest.IProductVariantService;
import hcmute.kltn.vtv.service.guest.IShopGuestService;
import hcmute.kltn.vtv.service.location.IDistanceLocationService;
import hcmute.kltn.vtv.service.vtv.shippingstrategy.*;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.user.request.CreateOrderUpdateRequest;
import hcmute.kltn.vtv.model.data.user.response.ListOrderResponse;
import hcmute.kltn.vtv.model.data.user.response.OrderResponse;
import hcmute.kltn.vtv.repository.user.OrderRepository;
import hcmute.kltn.vtv.service.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

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
    @Autowired
    private final IDistanceLocationService distanceLocationService;
    @Autowired
    private final IShopGuestService shopGuestService;
    @Autowired
    private final IProductVariantService productVariantService;


    @Override
    public OrderResponse createOrderByCartIds(String username, List<UUID> cartIds) {
        cartService.checkDuplicateCartIds(cartIds);
        cartService.checkListCartSameShop(username, cartIds);
        List<OrderItem> orderItems = orderItemService.createOrderItemsByCartIds(username, cartIds);
        Long shopId = orderItems.get(0).getCart().getProductVariant().getProduct().getShop().getShopId();
        Order order = createOrderByUsernameAndShopId(username, shopId);
        updateCreateOrderByOrderItemsPaymentMethod(order, orderItems, "COD");
        updateShippingInCreateOrder(order, "VTV Express");

        return OrderResponse.orderResponse(order, "Tạo đơn hàng mới thành công từ danh sách sản phẩm trong giỏ hàng.", "OK");
    }


    @Override
    public OrderResponse createOrderByMapProductVariantsAndQuantities(String username, Map<Long, Integer> productVariantIdsAndQuantities) {
        productVariantService.checkDuplicateProductVariantIds(new ArrayList<>(productVariantIdsAndQuantities.keySet()));
        Long shopId = getShopIdOfProductVariantId(new ArrayList<>(productVariantIdsAndQuantities.keySet()).get(0));
        productVariantService.checkProductVariantsSameShop(new ArrayList<>(productVariantIdsAndQuantities.keySet()), shopId);
        Order order = createOrderByUsernameAndShopId(username, shopId);
        List<OrderItem> orderItems = orderItemService.createOrderItemsByMapProductVariantIdsAndQuantities(order.getCustomer(),
                productVariantIdsAndQuantities);
        updateCreateOrderByOrderItemsPaymentMethod(order, orderItems, "COD");
        updateShippingInCreateOrder(order, "VTV Express");


        return OrderResponse.orderResponse(order, "Tạo đơn hàng mới thành công từ danh sách sản phẩm và số lượng.", "OK");
    }


    @Override
    public OrderResponse createOrderUpdate(CreateOrderUpdateRequest request) {
        Order order = createTemporaryOrderUpdate(request);

        return OrderResponse.orderResponse(order, "Cập nhật đơn hàng thành công.", "Success");
    }

    @Transactional
    @Override
    public OrderResponse saveOrder(CreateOrderUpdateRequest request) {
        Order order = createTemporaryOrderUpdate(request);

        // Sau này nếu có nhiều hình thước thanh toán sẻ sử lý ở đây.
        switch (order.getPaymentMethod()) {
            case "COD":
                order.setStatus(OrderStatus.PENDING);
                break;
            default:
                order.setStatus(OrderStatus.PENDING);
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


            return OrderResponse.orderResponse(save, "Đặt hàng thành công.", "Success");

        } catch (Exception e) {
            throw new BadRequestException("Đặt hàng thất bại! " + e.getMessage() + " " + e.getCause());
        }
    }

    @Override
    public ListOrderResponse getOrders(String username) {
        List<Order> orders = orderRepository.findAllByCustomerUsername(username)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy đơn hàng!"));
        return ListOrderResponse.listOrderResponse(orders, "Lấy danh sách đơn hàng thành công.", "OK");

    }

    @Override
    public ListOrderResponse getOrdersByStatus(String username, OrderStatus status) {
        List<Order> orders = orderRepository.findAllByCustomerUsernameAndStatus(username, status)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy đơn hàng!"));

        String message = messageByOrderStatus(status);

        return ListOrderResponse.listOrderResponse(orders, message, "OK");
    }

    @Override
    public OrderResponse getOrderDetail(String username, UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy đơn hàng!"));
        if (!order.getCustomer().getUsername().equals(username)) {
            throw new BadRequestException("Không tìm thấy đơn hàng!");
        }


        return OrderResponse.orderResponse(order, "Lấy chi tiết đơn hàng thành công.", "OK");

    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(String username, UUID orderId) {
        Order order = orderRepository.findByOrderIdAndStatus(orderId, OrderStatus.PENDING)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy đơn hàng!"));
        if (order == null || !order.getCustomer().getUsername().equals(username)) {
            throw new BadRequestException("Không tìm thấy đơn hàng!");
        }

        order.setStatus(OrderStatus.CANCEL);
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

            return OrderResponse.orderResponse(save, "Hủy đơn hàng thành công.", "Success");
        } catch (Exception e) {
            throw new BadRequestException("Hủy đơn hàng thất bại!");
        }
    }

    @Override
    public String messageByOrderStatus(OrderStatus status) {

        return switch (status) {
            case PENDING -> "Lấy danh sách đơn hàng đang chờ xử lý thành công.";
            case PROCESSING -> "Lấy danh sách đơn hàng đang xử lý thành công.";
            case SHIPPING -> "Lấy danh sách đơn hàng đang giao thành công.";
            case COMPLETED -> "Lấy danh sách đơn hàng đã giao thành công.";
            case CANCEL -> "Lấy danh sách đơn hàng đã hủy thành công.";
            case WAITING -> "Lấy danh sách đơn hàng yêu cầu xử lý thành công.";
            default -> "Lấy danh sách đơn hàng thành công.";
        };
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


    private Order createOrderByUsernameAndShopId(String username, Long shopId) {
        Customer customer = customerService.getCustomerByUsername(username);
        Address address = addressService.getAddressActiveByUsername(username);
        Shop shop = shopGuestService.getShopById(shopId);

        Order order = new Order();
        order.setCustomer(customer);
        order.setAddress(address);
        order.setShopId(shop.getShopId());
        order.setShopName(shop.getName());
        order.setShopWardCode(shop.getWard());

        return order;
    }


    private void updateCreateOrderByOrderItemsPaymentMethod(Order order, List<OrderItem> orderItems, String paymentMethod) {
        order.setOrderItems(orderItems);
        order.setStatus(OrderStatus.WAITING);
        order.setTotalPrice(getTotalPrice(orderItems));
        order.setVoucherOrders(null);
        order.setPaymentMethod(paymentMethod);
        order.setPaymentTotal(order.getTotalPrice());
        order.setDiscount(0L);
        order.setNote(null);
        order.setOrderDate(new Date());
        order.setCount(getTotalCount(orderItems));
    }


    private void updateShippingInCreateOrder(Order order, String shippingMethod) {
        Long shippingFee = shippingFeeByShippingMethodAndCustomerWardCodeAndShopWardCode(shippingMethod,
                order.getAddress().getWard().getWardCode(), order.getShopWardCode().getWardCode());

        order.setShippingMethod(shippingMethod);
        order.setShippingFee(shippingFee);
        order.setTotalPrice(order.getTotalPrice() + shippingFee);
    }

    private Long shippingFeeByShippingMethodAndCustomerWardCodeAndShopWardCode(String shippingMethod, String customerWardCode, String shopWardCode) {
        IShippingStrategy shippingStrategy = switch (shippingMethod) {
            case "GHTK" -> new GiaoHangTietKiemShippingStrategy();
            case "GHN" -> new GiaoHangNhanhShippingStrategy();
            case "VTV Express" -> new VtvExpressShippingStrategy();
            default -> throw new BadRequestException("Phương thức vận chuyển không hợp lệ.");
        };
        int distanceLocation = distanceLocationService.calculateDistance(customerWardCode, shopWardCode);

        return shippingStrategy.calculateShippingCost(distanceLocation);
    }


//    private Order createOrderByCartIds( Customer customer, Address address, Shop shop, List<UUID> cartIds) {
//        checkListCartSameShop(customer.getUsername(), cartIds);
//        List<OrderItem> orderItems = orderItemService.createOrderItems(customer.getUsername(), cartIds);
//
//        return createOrderByCustomerAndOrderItemsAndAddressAndShopAndShippingMethodAndPaymentMethod(
//                customer, orderItems, address, shop, "VTV Express", "COD");
//    }


    private Order createTemporaryOrder(String username, List<UUID> cartIds) {

        Customer customer = customerService.getCustomerByUsername(username);

//        checkListCartSameShop(username, cartIds);

        List<OrderItem> orderItems = orderItemService.createOrderItemsByCartIds(username, cartIds);
        Address address = addressService.getAddressActiveByUsername(username);

        Long totalPrice = getTotalPrice(orderItems);
        Long discount = 0L;

        IShippingStrategy shippingStrategy = new GiaoHangNhanhShippingStrategy();
        Long shippingFee = shippingStrategy.calculateShippingCost(totalPrice);

        Long totalPayment = totalPrice + shippingFee - discount;
        Long shopId = orderItems.get(0).getCart().getProductVariant().getProduct().getShop().getShopId();
        String shopName = orderItems.get(0).getCart().getProductVariant().getProduct().getShop()
                .getName();

        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderItems(orderItems);
        order.setAddress(address);
        order.setStatus(OrderStatus.WAITING);
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


    private Long getShopIdOfProductVariantId(Long productVariantId) {
        return productVariantService.getProductVariantById(productVariantId).getProduct().getShop().getShopId();
    }





}
