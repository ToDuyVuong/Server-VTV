package hcmute.kltn.vtv.controller.user;

import hcmute.kltn.vtv.model.data.user.request.OrderRequestWithCart;
import hcmute.kltn.vtv.model.data.user.request.OrderRequestWithProductVariant;
import hcmute.kltn.vtv.model.data.user.response.ListOrderResponse;
import hcmute.kltn.vtv.model.data.user.response.OrderItemResponse;
import hcmute.kltn.vtv.model.data.user.response.OrderResponse;
import hcmute.kltn.vtv.model.extra.OrderStatus;
import hcmute.kltn.vtv.service.user.IOrderService;
import hcmute.kltn.vtv.service.user.impl.OrderItemServiceImpl;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/customer/order")
@RequiredArgsConstructor
public class OrderController {

    @Autowired
    private final IOrderService orderService;



    @PostMapping("/create/by-cartIds")
    public ResponseEntity<OrderResponse> createOrderByCartIds(@RequestBody List<UUID> cartIds,
                                                              HttpServletRequest request) {
        if (cartIds.isEmpty()) {
            throw new NotFoundException("Danh sách mã giỏ hàng không được để trống!");
        }

        String username = (String) request.getAttribute("username");
        return ResponseEntity.ok(orderService.createOrderByCartIds(username, cartIds));
    }

    @PostMapping("/create-update/with-cart")
    public ResponseEntity<OrderResponse> createOrderWithCart(@RequestBody OrderRequestWithCart orderRequestWithCart,
                                                             HttpServletRequest request) {
        OrderRequestWithCart.validate(orderRequestWithCart);

        String username = (String) request.getAttribute("username");
        return ResponseEntity.ok(orderService.createOrderWithCart(orderRequestWithCart, username));
    }


    @PostMapping("/create/by-product-variant")
    public ResponseEntity<OrderResponse> createOrderByProductVariantIdsAndQuantities(@RequestBody Map<Long, Integer> productVariantIdsAndQuantities,
                                                                                     HttpServletRequest request) {
        if (productVariantIdsAndQuantities.isEmpty()) {
            throw new NotFoundException("Danh sách mã sản phẩm không được để trống!");
        }

        productVariantIdsAndQuantities.forEach((k, v) -> {
            if (k == null || v == null) {
                throw new BadRequestException("Danh sách sản phẩm không hợp lệ!");
            }
            if (v <= 0) {
                throw new BadRequestException("Số lượng biến thể sản phẩm của mã biến thể sản phẩm " + k + " không hợp lệ!");
            }
        });

        String username = (String) request.getAttribute("username");
        return ResponseEntity.ok(orderService.createOrderByMapProductVariantsAndQuantities(username, productVariantIdsAndQuantities));
    }


    @PostMapping("/create-update/with-product-variant")
    public ResponseEntity<OrderResponse> createOrderWithProductVariant(@RequestBody OrderRequestWithProductVariant orderRequestWithProductVariant,
                                                                       HttpServletRequest request) {
        OrderRequestWithProductVariant.validate(orderRequestWithProductVariant);
        String username = (String) request.getAttribute("username");

        return ResponseEntity.ok(orderService.createOrderWithProductVariant(orderRequestWithProductVariant, username));
    }


    @PostMapping("/add/with-cart")
    public ResponseEntity<OrderResponse> addNewOrderWithCart(@RequestBody OrderRequestWithCart orderRequestWithCart,
                                                             HttpServletRequest request) {
        OrderRequestWithCart.validate(orderRequestWithCart);
        String username = (String) request.getAttribute("username");

        return ResponseEntity.ok(orderService.addNewOrderWithCart(orderRequestWithCart, username));
    }

    @PostMapping("/add/with-product-variant")
    public ResponseEntity<OrderResponse> addNewOrderWithProductVariant(@RequestBody OrderRequestWithProductVariant orderRequestWithProductVariant,
                                                                       HttpServletRequest request) {
        OrderRequestWithProductVariant.validate(orderRequestWithProductVariant);
        String username = (String) request.getAttribute("username");

        return ResponseEntity.ok(orderService.addNewOrderWithProductVariant(orderRequestWithProductVariant, username));
    }


    @GetMapping("/list")
    public ResponseEntity<ListOrderResponse> getOrders(HttpServletRequest requestHttp) {
        String username = (String) requestHttp.getAttribute("username");
        return ResponseEntity.ok(orderService.getOrders(username));
    }


    @GetMapping("/list/status/{status}")
    public ResponseEntity<ListOrderResponse> getOrdersByStatus(@PathVariable OrderStatus status,
                                                               HttpServletRequest requestHttp) {
        String username = (String) requestHttp.getAttribute("username");
        return ResponseEntity.ok(orderService.getOrdersByStatus(username, status));
    }


    @GetMapping("/detail/{orderId}")
    public ResponseEntity<OrderResponse> getOrderDetail(@PathVariable UUID orderId,
                                                        HttpServletRequest requestHttp) {
        String username = (String) requestHttp.getAttribute("username");
        return ResponseEntity.ok(orderService.getOrderDetail(username, orderId));
    }


    @PatchMapping("/cancel/{orderId}")
    public ResponseEntity<OrderResponse> cancelOrderById(@PathVariable UUID orderId,
                                                                  HttpServletRequest requestHttp) {
        String username = (String) requestHttp.getAttribute("username");
        return ResponseEntity.ok(orderService.cancelOrderById(username, orderId));
    }


    @PatchMapping("/complete/{orderId}")
    public ResponseEntity<OrderResponse> completeOrderById(@PathVariable UUID orderId,
                                                           HttpServletRequest requestHttp) {
        String username = (String) requestHttp.getAttribute("username");
        return ResponseEntity.ok(orderService.completeOrderById(username, orderId));
    }

//    @GetMapping("/order-item/detail/{orderItemId}")
//    public ResponseEntity<OrderItemResponse> getOrderItemByOrderItemId(@PathVariable UUID orderItemId,
//                                                                       HttpServletRequest requestHttp) {
//        return ResponseEntity.ok(orderItemService.getOrderItemByOrderItemId(orderItemId));
//    }

}
