package hcmute.kltn.vtv.service.user.impl;

import hcmute.kltn.vtv.model.entity.user.Cart;
import hcmute.kltn.vtv.model.entity.user.Customer;
import hcmute.kltn.vtv.model.entity.user.Order;
import hcmute.kltn.vtv.model.entity.user.OrderItem;
import hcmute.kltn.vtv.model.entity.vendor.ProductVariant;
import hcmute.kltn.vtv.model.extra.CartStatus;
import hcmute.kltn.vtv.model.extra.OrderStatus;
import hcmute.kltn.vtv.service.guest.IProductVariantService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.user.response.OrderItemResponse;
import hcmute.kltn.vtv.repository.user.CartRepository;
import hcmute.kltn.vtv.repository.user.OrderItemRepository;
import hcmute.kltn.vtv.service.user.ICartService;
import hcmute.kltn.vtv.service.user.IOrderItemService;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements IOrderItemService {

    @Autowired
    private final OrderItemRepository orderItemRepository;
    @Autowired
    private final ICartService cartService;
    @Autowired
    private final IProductVariantService productVariantService;
    @Autowired
    private final CartRepository cartRepository;


    @Override
    public OrderItem getOrderItemById(UUID orderItemId) {
        return orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm của đơn hàng theo mã sản phẩm của đơn hàng!"));
    }

    @Override
    public OrderItemResponse getOrderItemByOrderItemId(UUID orderItemId) {
        OrderItem orderItem = getOrderItemById(orderItemId);
        return OrderItemResponse.orderItemResponse(orderItem, "Lấy thông tin chi tiết về sản phẩm trong đơn hàng thành công!", "OK");
    }

    public OrderItem createOrderItem(UUID cartId, String username) {
        OrderItem orderItem = new OrderItem();
        Cart cart = cartService.getCartByCartIdAndUsername(cartId, username);
        orderItem.setOrder(null);
        orderItem.setCart(cart);
        orderItem.setPrice(cart.getProductVariant().getPrice());
        return orderItem;
    }


    @Transactional
    public OrderItem addNewOrderItem(Order order, UUID cartId, String username) {
        OrderItem orderItem = new OrderItem();
        Cart cart = cartService.getCartByCartIdAndUsername(cartId, username);
        orderItem.setOrder(order);
        orderItem.setCart(cart);
        orderItem.setPrice(cart.getProductVariant().getPrice());

        try {
            productVariantService.updateProductVariantQuantity(cart.getProductVariant().getProductVariantId(), -cart.getQuantity());
            cartService.updateOrderCart(cartId, username, CartStatus.ORDER);

            return orderItemRepository.save(orderItem);
        } catch (Exception e) {
            throw new BadRequestException("Thêm mới sản phẩm vào đơn hàng thất bại!");
        }
    }


    @Override
    @Transactional
    public List<OrderItem> addNewOrderItemsByCartIds(Order order, List<UUID> cartIds, String username) {
        List<OrderItem> orderItems = new ArrayList<>();
        try {
            cartIds.forEach(cartId -> orderItems.add(addNewOrderItem(order, cartId, username)));

            return orderItems;
        } catch (Exception e) {
            throw new BadRequestException("Thêm mới sản phẩm vào đơn hàng thất bại!");
        }
    }


    @Override
    @Transactional
    public List<OrderItem> addNewOrderItemsByyMapProductVariant(Order order, Map<Long, Integer> productVariantsAndQuantities, String username) {
        List<OrderItem> orderItems = new ArrayList<>();
        try {
            List<UUID> cartIds = cartService.getCartIdsAfterAddNewCartsByMapProductVariantIdAndQuantity(productVariantsAndQuantities, username);
            cartIds.forEach(cartId -> orderItems.add(addNewOrderItem(order, cartId, username)));

            return orderItems;
        } catch (Exception e) {
            throw new InternalServerErrorException("Thêm mới từ mã biến thể sản phẩm vào đơn hàng thất bại! " + e.getMessage());
        }


    }


    @Override
    public List<OrderItem> createOrderItemsByCartIds(String username, List<UUID> cartIds) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (UUID cartId : cartIds) {
            orderItems.add(createOrderItem(cartId, username));
        }

        return orderItems;
    }


    public OrderItem createOrderItemByProductVariantIdAndQuantity(Customer customer, Long productVariantId, int quantity) {
        ProductVariant productVariant = productVariantService
                .checkAndProductVariantAvailableWithQuantity(productVariantId, quantity);
        OrderItem orderItem = new OrderItem();
        orderItem.setCart(cartService.createCartByProductVariant(productVariant, quantity, customer));
        orderItem.setPrice(orderItem.getCart().getProductVariant().getPrice());

        return orderItem;
    }


    @Override
    public List<OrderItem> createOrderItemsByMapProductVariantIdsAndQuantities(Customer customer, Map<Long, Integer> mapProductVariantIdsAndQuantities) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : mapProductVariantIdsAndQuantities.entrySet()) {
            orderItems.add(createOrderItemByProductVariantIdAndQuantity(customer, entry.getKey(), entry.getValue()));
        }

        return orderItems;
    }


    @Transactional
    @Override
    public List<OrderItem> cancelOrderItem(Order order) {
        List<OrderItem> orderItems = new ArrayList<>();
        try {
            for (OrderItem orderItem : order.getOrderItems()) {
                Cart cart = orderItem.getCart();
                cart.setStatus(CartStatus.CANCEL);
                cart.setUpdateAt(order.getUpdateAt());

                productVariantService.updateProductVariantQuantity(cart.getProductVariant().getProductVariantId(), cart.getQuantity());
                cartRepository.save(cart);
                orderItems.add(orderItem);
            }

            return orderItems;
        } catch (Exception e) {
            throw new InternalServerErrorException("Cập nhật trạng thái giỏ hàng thất bại!");
        }

    }


    @Override
    public void checkExistOrderItem(UUID orderItemId) {
        if (!orderItemRepository.existsById(orderItemId)) {
            throw new NotFoundException("Mã sản phẩm của đơn hàng không tồn tại!");
        }
    }

    @Override
    public void checkExistOrderItemByIdAndUsername(UUID orderItemId, String username) {
        if (!orderItemRepository.existsByOrderItemIdAndCartCustomerUsername(orderItemId, username)) {
            throw new NotFoundException("Bạn không có quyền truy cập sản phẩm của đơn hàng này!");
        }
    }


    @Override
    public void checkOrderCompletedBeforeReview(UUID orderItemId) {
        if (!orderItemRepository.existsByOrderItemIdAndOrderStatus(orderItemId, OrderStatus.COMPLETED)) {
            throw new BadRequestException("Đơn hàng chưa hoàn thành. Bạn không thể đánh giá sản phẩm!");
        }
    }


}
