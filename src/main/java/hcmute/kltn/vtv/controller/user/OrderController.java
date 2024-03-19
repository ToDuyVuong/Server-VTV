package hcmute.kltn.vtv.controller.user;

import hcmute.kltn.vtv.model.data.user.request.CreateOrderUpdateRequest;
import hcmute.kltn.vtv.model.data.user.request.OrderRequestWithCartIds;
import hcmute.kltn.vtv.model.data.user.request.OrderRequestWithProductVariant;
import hcmute.kltn.vtv.model.data.user.response.ListOrderResponse;
import hcmute.kltn.vtv.model.data.user.response.OrderItemResponse;
import hcmute.kltn.vtv.model.data.user.response.OrderResponse;
import hcmute.kltn.vtv.model.extra.OrderStatus;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.service.user.IOrderService;
import hcmute.kltn.vtv.service.user.impl.OrderItemServiceImpl;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/customer/order")
@RequiredArgsConstructor
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private OrderItemServiceImpl orderItemService;


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
    public ResponseEntity<OrderResponse> createOrderWithCart(@RequestBody OrderRequestWithCartIds orderRequestWithCartIds,
                                                              HttpServletRequest request) {
        OrderRequestWithCartIds.validate(orderRequestWithCartIds);

        String username = (String) request.getAttribute("username");
        return ResponseEntity.ok(orderService.createOrderByOrderRequestWithCartIds(orderRequestWithCartIds, username));
    }


    @PostMapping("/add/with-cart")
    public ResponseEntity<OrderResponse> addNewOrderByOrderRequestWithCart(@RequestBody OrderRequestWithCartIds orderRequestWithCartIds,
                                                             HttpServletRequest request) {
        OrderRequestWithCartIds.validate(orderRequestWithCartIds);

        String username = (String) request.getAttribute("username");
        return ResponseEntity.ok(orderService.addNewOrderByOrderRequestWithCart(orderRequestWithCartIds, username));
    }


    @PostMapping("/create/by-product-variant")
    public ResponseEntity<OrderResponse> createOrderByProductVariantIdsAndQuantities(@RequestBody Map<Long, Integer> productVariantIdsAndQuantities,
                                                                                     HttpServletRequest request) {
        if (productVariantIdsAndQuantities.isEmpty()) {
            throw new NotFoundException("Danh sách mã sản phẩm không được để trống!");
        }

        productVariantIdsAndQuantities.forEach((k, v) -> {
            if (k == null || v == null ) {
                throw new BadRequestException("Danh sách sản phẩm không hợp lệ!");
            }
            if (v <= 0){
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

        return ResponseEntity.ok(orderService.createOrderByOrderRequestWithProductVariant(orderRequestWithProductVariant, username));
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

    @PostMapping("/cancel/{orderId}")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable UUID orderId,
                                                     HttpServletRequest requestHttp) {
        String username = (String) requestHttp.getAttribute("username");
        return ResponseEntity.ok(orderService.cancelOrder(username, orderId));
    }

    @GetMapping("/order-item/detail/{orderItemId}")
    public ResponseEntity<OrderItemResponse> getOrderItemByOrderItemId(@PathVariable UUID orderItemId,
                                                                       HttpServletRequest requestHttp) {
        return ResponseEntity.ok(orderItemService.getOrderItemByOrderItemId(orderItemId));
    }

}
