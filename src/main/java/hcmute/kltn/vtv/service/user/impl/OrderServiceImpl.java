package hcmute.kltn.vtv.service.user.impl;

import hcmute.kltn.vtv.model.data.user.request.OrderRequestWithCart;
import hcmute.kltn.vtv.model.data.user.request.OrderRequestWithProductVariant;
import hcmute.kltn.vtv.model.dto.shipping.ShippingDTO;
import hcmute.kltn.vtv.model.entity.user.*;
import hcmute.kltn.vtv.model.entity.vendor.Shop;
import hcmute.kltn.vtv.model.entity.wallet.LoyaltyPoint;
import hcmute.kltn.vtv.model.entity.wallet.LoyaltyPointHistory;
import hcmute.kltn.vtv.model.extra.OrderStatus;
import hcmute.kltn.vtv.service.guest.IProductVariantService;
import hcmute.kltn.vtv.service.guest.IShopGuestService;
import hcmute.kltn.vtv.service.location.IDistanceLocationService;
import hcmute.kltn.vtv.service.shipping.IShippingService;
import hcmute.kltn.vtv.service.vtv.shippingstrategy.*;
import hcmute.kltn.vtv.service.wallet.ILoyaltyPointHistoryService;
import hcmute.kltn.vtv.service.wallet.ILoyaltyPointService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.user.response.ListOrderResponse;
import hcmute.kltn.vtv.model.data.user.response.OrderResponse;
import hcmute.kltn.vtv.repository.user.OrderRepository;
import hcmute.kltn.vtv.service.user.*;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private final OrderRepository orderRepository;
    private final IOrderItemService orderItemService;
    private final IVoucherOrderService voucherOrderService;
    private final ICartService cartService;
    private final ICustomerService customerService;
    private final IAddressService addressService;
    private final IDistanceLocationService distanceLocationService;
    private final IProductVariantService productVariantService;
    private final IVoucherCustomerService voucherCustomerService;
    private final IShippingService shippingService;
    private final ILoyaltyPointService loyaltyPointService;
    private final ILoyaltyPointHistoryService loyaltyPointHistoryService;
    private final IMailService mailService;


    @Override
    @Transactional
    public OrderResponse createOrderByCartIds(String username, List<UUID> cartIds) {
        cartService.checkDuplicateCartIds(cartIds);
        cartService.checkListCartSameShop(username, cartIds);

        List<OrderItem> orderItems = orderItemService.createOrderItemsByCartIds(username, cartIds);
        Address address = addressService.getAddressActiveByUsername(username);

        Order order = createBaseOrder(username, address);
        updateCreateOrderByOrderItemsPaymentMethod(order, orderItems, "COD");

        updateLoyaltyPointHistoryInCreateOrder(order);
        updateShippingInCreateOrder(order, "VTV Express");

        ShippingDTO shippingDTO = shippingService.getCalculateShippingByWardAndTransportProvider(address.getWard().getWardCode(),
                order.getShop().getWard().getWardCode(), "VTV Express").getShippingDTO();

        return OrderResponse.orderResponse(order, shippingDTO, "Tạo đơn hàng mới thành công từ danh sách sản phẩm trong giỏ hàng.", "OK");
    }


    @Override
    @Transactional
    public OrderResponse createOrderByMapProductVariantsAndQuantities(String username, Map<Long, Integer> productVariantIdsAndQuantities) {
        productVariantService.checkDuplicateProductVariantIds(new ArrayList<>(productVariantIdsAndQuantities.keySet()));
        Long shopId = getShopIdOfProductVariantId(new ArrayList<>(productVariantIdsAndQuantities.keySet()).get(0));
        productVariantService.checkProductVariantsSameShop(new ArrayList<>(productVariantIdsAndQuantities.keySet()), shopId);
        Address address = addressService.getAddressActiveByUsername(username);

        Order order = createBaseOrder(username, address);
        List<OrderItem> orderItems = orderItemService.createOrderItemsByMapProductVariantIdsAndQuantities(order.getCustomer(),
                productVariantIdsAndQuantities);
        updateCreateOrderByOrderItemsPaymentMethod(order, orderItems, "COD");
        updateLoyaltyPointHistoryInCreateOrder(order);
        updateShippingInCreateOrder(order, "VTV Express");

        ShippingDTO shippingDTO = shippingService.getCalculateShippingByWardAndTransportProvider(address.getWard().getWardCode(),
                order.getShop().getWard().getWardCode(), "VTV Express").getShippingDTO();

        return OrderResponse.orderResponse(order, shippingDTO, "Tạo đơn hàng mới thành công từ danh sách sản phẩm và số lượng.", "OK");
    }


    @Override
    @Transactional
    public OrderResponse createOrderWithProductVariant(OrderRequestWithProductVariant request, String username) {
        Long shopId = getShopIdOfProductVariantId(new ArrayList<>(request.getProductVariantIdsAndQuantities().keySet()).get(0));

        productVariantService.checkProductVariantsSameShop(new ArrayList<>(request.getProductVariantIdsAndQuantities().keySet()), shopId);
        Address address = addressService.checkAddress(request.getAddressId(), username);

        Order order = createOrderByOrderRequestWithProductVariant(request, username, address);

        ShippingDTO shippingDTO = shippingService.getCalculateShippingByWardAndTransportProvider(address.getWard().getWardCode(),
                order.getShop().getWard().getWardCode(), "VTV Express").getShippingDTO();

        return OrderResponse.orderResponse(order, shippingDTO, "Tạo đơn hàng mới thành công từ danh sách sản phẩm và số lượng.", "OK");
    }


    @Override
    @Transactional
    public OrderResponse createOrderWithCart(OrderRequestWithCart request, String username) {
        cartService.checkDuplicateCartIds(request.getCartIds());
        cartService.checkListCartSameShop(username, request.getCartIds());
        Address address = addressService.checkAddress(request.getAddressId(), username);

        Order order = createOrderByOrderRequestWithCartIds(request, username, address);

        ShippingDTO shippingDTO = shippingService.getCalculateShippingByWardAndTransportProvider(address.getWard().getWardCode(),
                order.getShop().getWard().getWardCode(), "VTV Express").getShippingDTO();

        return OrderResponse.orderResponse(order, shippingDTO, "Tạo đơn hàng mới thành công từ danh sách sản phẩm trong giỏ hàng.", "OK");
    }


    @Override
    @Transactional
    public OrderResponse addNewOrderWithCart(OrderRequestWithCart request, String username) {
        cartService.checkDuplicateCartIds(request.getCartIds());
        cartService.checkListCartSameShop(username, request.getCartIds());
        Order order = createAddNewOrderWithCart(request, username);

        try {
            addOrderItemsToOrder(order, request.getCartIds(), username);
            addVoucherOrderToOrder(order, request.getShopVoucherCode(), request.getSystemVoucherCode());
            if (request.isUseLoyaltyPoint()) {
                addLoyaltyPointHistoryToOrder(order);
            }
            updateShippingInCreateOrder(order, request.getShippingMethod());
            orderRepository.save(order);

            ShippingDTO shippingDTO = shippingService.getCalculateShippingByWardAndTransportProvider(order.getAddress().getWard().getWardCode(),
                    order.getShop().getWard().getWardCode(), request.getShippingMethod()).getShippingDTO();

            String messageMail = "Đặt hàng thành công.";
            mailService.sendOrderConfirmationEmail(order, shippingDTO, messageMail);

            return OrderResponse.orderResponse(order, shippingDTO, "Đặt hàng thành công từ danh sách sản phẩm.", "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Đặt hàng thất bại từ danh sách sản phẩm! " + e.getMessage());
        }
    }


    @Override
    @Transactional
    public OrderResponse addNewOrderWithProductVariant(OrderRequestWithProductVariant request, String username) {
        Long shopId = getShopIdOfProductVariantId(new ArrayList<>(request.getProductVariantIdsAndQuantities().keySet()).get(0));
        productVariantService.checkProductVariantsSameShop(new ArrayList<>(request.getProductVariantIdsAndQuantities().keySet()), shopId);
        Order order = createAddNewOrderWithCart(OrderRequestWithCart.convertWithProductVariantToWithCart(request), username);

        try {
            addOrderItemsToOrderByMapProductVariant(order, request.getProductVariantIdsAndQuantities(), username);
            addVoucherOrderToOrder(order, request.getShopVoucherCode(), request.getSystemVoucherCode());
            if (request.isUseLoyaltyPoint()) {
                addLoyaltyPointHistoryToOrder(order);
            }
            updateShippingInCreateOrder(order, request.getShippingMethod());
            orderRepository.save(order);

            ShippingDTO shippingDTO = shippingService.getCalculateShippingByWardAndTransportProvider(order.getAddress().getWard().getWardCode(),
                    order.getShop().getWard().getWardCode(), request.getShippingMethod()).getShippingDTO();

            String messageMail = "Đặt hàng thành công.";
            mailService.sendOrderConfirmationEmail(order, shippingDTO, messageMail);

            return OrderResponse.orderResponse(order, shippingDTO, "Đặt hàng thành công từ danh sách sản phẩm trong giỏ hàng.", "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Đặt hàng thất bại từ danh sách sản phẩm trong giỏ hàng! " + e.getMessage());
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

        ShippingDTO shippingDTO = shippingService.getCalculateShippingByWardAndTransportProvider(order.getAddress().getWard().getWardCode(),
                order.getShop().getWard().getWardCode(), order.getShippingMethod()).getShippingDTO();


        return OrderResponse.orderResponse(order, shippingDTO, "Lấy chi tiết đơn hàng thành công.", "OK");

    }


    //
    @Override
    @Transactional
    public OrderResponse completeOrderById(String username, UUID orderId) {
        Order order = orderRepository.findByOrderIdAndCustomerUsername(orderId, username)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng theo mã đơn hàng của tài khoản!"));

        if (order.getStatus().equals(OrderStatus.SHIPPING) || order.getStatus().equals(OrderStatus.DELIVERED)) {
            order.setStatus(OrderStatus.COMPLETED);
            order.setUpdateAt(LocalDateTime.now());
            try {
                orderRepository.save(order);
                ShippingDTO shippingDTO = shippingService.getCalculateShippingByWardAndTransportProvider(order.getAddress().getWard().getWardCode(),
                        order.getShop().getWard().getWardCode(), order.getShippingMethod()).getShippingDTO();
                String messageMail = "Xác nhận đã nhận hàng thành công.";
                mailService.sendOrderConfirmationEmail(order, shippingDTO, messageMail);
                return OrderResponse.orderResponse(order, "Xác nhận đã nhận hàng thành công.", "Success");
            } catch (Exception e) {
                throw new InternalServerErrorException("Hoàn thành đơn hàng thất bại!");
            }
        }

        throw new BadRequestException("Không thể hoàn thành đơn hàng! Chỉ có thể hoàn thành đơn hàng đang giao hàng hoặc đã giao hàng.");
    }

    @Override
    @Transactional
    public OrderResponse cancelOrderById(String username, UUID orderId) {
        Order order = orderRepository.findByOrderIdAndCustomerUsername(orderId, username)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng theo mã đơn hàng của tài khoản!"));

        if (order.getStatus().equals(OrderStatus.PENDING)) {
            return cancelOrder(order);
        }
        if (order.getStatus().equals(OrderStatus.PROCESSING)) {
            return waitingOrder(order);
        }

        throw new BadRequestException("Không thể hủy đơn hàng! Chỉ có thể hủy đơn hàng đang chờ xử lý hoặc yêu cầu hủy đơn hàng đang xử lý.");
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


    @Transactional
    public OrderResponse cancelOrder(Order order) {
        order.setStatus(OrderStatus.CANCEL);
        order.setUpdateAt(LocalDateTime.now());
        try {
            orderRepository.save(order);

            if (order.getVoucherOrders() != null) {
                for (VoucherOrder voucherOrder : order.getVoucherOrders()) {
                    voucherOrderService.cancelVoucherOrder(voucherOrder.getVoucherOrderId());
                }
            }
            if (order.getLoyaltyPointHistory() != null) {
                Long point = -order.getLoyaltyPointHistory().getPoint();
                loyaltyPointService.updatePointInLoyaltyPointByUsername(order.getCustomer().getUsername(), point, "REFUND");
            }
            List<OrderItem> orderItems = orderItemService.cancelOrderItem(order);
            order.setOrderItems(orderItems);
            ShippingDTO shippingDTO = shippingService.getCalculateShippingByWardAndTransportProvider(order.getAddress().getWard().getWardCode(),
                    order.getShop().getWard().getWardCode(), order.getShippingMethod()).getShippingDTO();
            String messageMail = "Hủy đơn hàng thành công.";
            mailService.sendOrderConfirmationEmail(order, shippingDTO, messageMail);

            return OrderResponse.orderResponse(order, "Hủy đơn hàng thành công.", "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Hủy đơn hàng thất bại!");
        }
    }


    @Transactional
    public OrderResponse waitingOrder(Order order) {
        order.setStatus(OrderStatus.WAITING);
        order.setUpdateAt(LocalDateTime.now());
        try {
            Order save = orderRepository.save(order);

            ShippingDTO shippingDTO = shippingService.getCalculateShippingByWardAndTransportProvider(order.getAddress().getWard().getWardCode(),
                    order.getShop().getWard().getWardCode(), order.getShippingMethod()).getShippingDTO();
            String messageMail = "Yêu cầu hủy đơn hàng thành công.";
            mailService.sendOrderConfirmationEmail(order, shippingDTO, messageMail);


            return OrderResponse.orderResponse(save, "Yêu cầu hủy đơn hàng thành công.", "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Yêu cầu hủy đơn hàng thất bại!");
        }
    }


    private Order createOrderByOrderRequestWithCartIds(OrderRequestWithCart request, String username, Address address) {
        List<OrderItem> orderItems = orderItemService.createOrderItemsByCartIds(username, request.getCartIds());
        Order order = createBaseOrder(username, address);
        updateCreateOrderByOrderItemsPaymentMethod(order, orderItems, request.getPaymentMethod());
        if (request.isUseLoyaltyPoint()) {
            updateLoyaltyPointHistoryInCreateOrder(order);
        }
        if (!request.getNote().isEmpty()) {
            order.setNote(request.getNote());
        }
        updateCreateOrderByVoucherOrder(order, request.getShopVoucherCode(), request.getSystemVoucherCode());
        updateShippingInCreateOrder(order, request.getShippingMethod());

        return order;
    }


    private Order createOrderByOrderRequestWithProductVariant(OrderRequestWithProductVariant request, String username, Address address) {
        Order order = createBaseOrder(username, address);
        List<OrderItem> orderItems = orderItemService.createOrderItemsByMapProductVariantIdsAndQuantities(order.getCustomer(),
                request.getProductVariantIdsAndQuantities());
        updateCreateOrderByOrderItemsPaymentMethod(order, orderItems, request.getPaymentMethod());
        if (request.isUseLoyaltyPoint()) {
            updateLoyaltyPointHistoryInCreateOrder(order);
        }
        if (!request.getNote().isEmpty()) {
            order.setNote(request.getNote());
        }
        updateCreateOrderByVoucherOrder(order, request.getShopVoucherCode(), request.getSystemVoucherCode());
        updateShippingInCreateOrder(order, request.getShippingMethod());

        return order;
    }


    private Order createAddNewOrderWithCart(OrderRequestWithCart request, String username) {
        Address address = addressService.checkAddress(request.getAddressId(), username);
        Order order = createBaseOrder(username, address);
        order.setStatus(OrderStatus.PENDING);
        if (!request.getNote().isEmpty()) {
            order.setNote(request.getNote());
        }
        try {
            return orderRepository.save(order);
        } catch (Exception e) {
            throw new InternalServerErrorException("Tạo mẫu đơn hàng mới thất bại! " + e.getMessage());
        }
    }


    private Order createBaseOrder(String username, Address address) {
        Order order = new Order();
        order.setCustomer(customerService.getCustomerByUsername(username));
        order.setAddress(address);
        order.setDiscountShop(0L);
        order.setDiscountSystem(0L);
        order.setPaymentMethod("COD");
        order.setPaymentTotal(0L);
        order.setNote(null);
        order.setCreateAt(LocalDateTime.now());
        order.setUpdateAt(LocalDateTime.now());
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.WAITING);

        return order;
    }


    private void updateCreateOrderByOrderItemsPaymentMethod(Order order, List<OrderItem> orderItems, String paymentMethod) {
        order.setShop(orderItems.get(0).getCart().getProductVariant().getProduct().getShop());
        order.setOrderItems(orderItems);
        order.setTotalPrice(getTotalPrice(orderItems));
        order.setVoucherOrders(null);
        order.setPaymentMethod(paymentMethod);
        order.setShop(orderItems.get(0).getCart().getProductVariant().getProduct().getShop());
        order.setOrderDate(new Date());
        order.setCount(getTotalCount(orderItems));
    }


    private void updateCreateOrderByVoucherOrder(Order order, String shopVoucherCode, String systemVoucherCode) {
        List<VoucherOrder> voucherOrders = new ArrayList<>();
        VoucherOrder voucherOrder;
        if (shopVoucherCode != null) {
            voucherOrder = voucherOrderService.createVoucherOrder(shopVoucherCode, order.getShop().getShopId());
            voucherOrders.add(voucherOrder);
            Long discount = voucherCustomerService
                    .discountVoucherByShopVoucherCodeAndShopIdAndTotalPrice(
                            shopVoucherCode,
                            order.getShop().getShopId(),
                            order.getTotalPrice());
            if (discount > order.getTotalPrice()) {
                discount = order.getTotalPrice() - order.getTotalPrice() * 96 / 100;
            }
            order.setDiscountShop(-discount);
        }

        if (systemVoucherCode != null) {
            voucherOrder = voucherOrderService.createVoucherOrder(systemVoucherCode, null);
            voucherOrders.add(voucherOrder);
            Long discount = voucherCustomerService
                    .discountVoucherBySystemVoucherCodeAndTotalPrice(
                            systemVoucherCode,
                            order.getTotalPrice());
            order.setDiscountSystem(-discount);
        }
        order.setVoucherOrders(voucherOrders);
    }

    private void updateShippingInCreateOrder(Order order, String shippingMethod) {
        Long shippingFee = shippingFeeByShippingMethodAndCustomerWardCodeAndShopWardCode(shippingMethod,
                order.getAddress().getWard().getWardCode(), order.getShop().getWard().getWardCode());
        Long loyaltyPoint = order.getLoyaltyPointHistory() != null ? order.getLoyaltyPointHistory().getPoint() : 0;
        order.setShippingMethod(shippingMethod);
        order.setShippingFee(shippingFee);
        order.setPaymentTotal(order.getTotalPrice() + shippingFee + order.getDiscountShop() + order.getDiscountSystem() + loyaltyPoint);
        if (order.getPaymentTotal() < 0) {
            order.setPaymentTotal(0L);
        }
    }


    private void updateLoyaltyPointHistoryInCreateOrder(Order order) {
        LoyaltyPoint loyaltyPoint = loyaltyPointService.getLoyaltyPointByUsername(order.getCustomer().getUsername());

        Long point = order.getTotalPrice() <= loyaltyPoint.getTotalPoint() ? order.getTotalPrice() : loyaltyPoint.getTotalPoint();
        LoyaltyPointHistory loyaltyPointHistory = new LoyaltyPointHistory();
        loyaltyPointHistory.setPoint(point);
        loyaltyPointHistory.setLoyaltyPoint(loyaltyPoint);

        order.setLoyaltyPointHistory(loyaltyPointHistory);
        order.setPaymentTotal(order.getTotalPrice() - loyaltyPointHistory.getPoint());
    }


    private void addOrderItemsToOrder(Order order, List<UUID> cartIds, String username) {
        List<OrderItem> orderItems = orderItemService.addNewOrderItemsByCartIds(order, cartIds, username);
        order.setShop(orderItems.get(0).getCart().getProductVariant().getProduct().getShop());
        order.setOrderItems(orderItems);
        order.setTotalPrice(getTotalPrice(orderItems));
        order.setCount(getTotalCount(orderItems));
    }


    private void addOrderItemsToOrderByMapProductVariant(Order order, Map<Long, Integer> productVariantsAndQuantities, String username) {
        List<OrderItem> orderItems = orderItemService.addNewOrderItemsByyMapProductVariant(order, productVariantsAndQuantities, username);
        order.setShop(orderItems.get(0).getCart().getProductVariant().getProduct().getShop());
        order.setOrderItems(orderItems);
        order.setTotalPrice(getTotalPrice(orderItems));
        order.setCount(getTotalCount(orderItems));
    }


    private void addVoucherOrderToOrder(Order order, String shopVoucherCode, String systemVoucherCode) {
        List<VoucherOrder> voucherOrders = new ArrayList<>();
        VoucherOrder voucherOrder;
        if (shopVoucherCode != null) {
            voucherOrder = voucherOrderService.addNewVoucherOrderByCode(shopVoucherCode, order, order.getShop().getShopId());
            voucherOrders.add(voucherOrder);
            Long discount = voucherCustomerService
                    .discountVoucherByShopVoucherCodeAndShopIdAndTotalPrice(
                            shopVoucherCode,
                            order.getShop().getShopId(),
                            order.getTotalPrice());
            if (discount > order.getTotalPrice()) {
                discount = order.getTotalPrice() - order.getTotalPrice() * 96 / 100;
            }
            order.setDiscountShop(-discount);
        }

        if (systemVoucherCode != null) {
            voucherOrder = voucherOrderService.addNewVoucherOrderByCode(systemVoucherCode, order, null);
            voucherOrders.add(voucherOrder);
            Long discount = voucherCustomerService
                    .discountVoucherBySystemVoucherCodeAndTotalPrice(
                            systemVoucherCode,
                            order.getTotalPrice());
            order.setDiscountSystem(-discount);
        }
        order.setVoucherOrders(voucherOrders);
    }


    private void addLoyaltyPointHistoryToOrder(Order order) {
        LoyaltyPoint loyaltyPoint = loyaltyPointService.getLoyaltyPointByUsername(order.getCustomer().getUsername());
        if (loyaltyPoint.getTotalPoint() == 0) {
            throw new BadRequestException("Không đủ điểm thưởng để thanh toán. Điểm thưởng hiện tại của bạn là 0.");
        }
        Long point = -(order.getTotalPrice() <= loyaltyPoint.getTotalPoint() ? order.getTotalPrice() : loyaltyPoint.getTotalPoint());
        LoyaltyPointHistory loyaltyPointHistory = loyaltyPointService.updatePointInLoyaltyPointByUsername(order.getCustomer().getUsername(), point, "PAYMENT");

        order.setLoyaltyPointHistory(loyaltyPointHistory);
        order.setPaymentTotal(order.getTotalPrice() - loyaltyPointHistory.getPoint());
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
