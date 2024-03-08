package hcmute.kltn.vtv.controller.user;

import hcmute.kltn.vtv.model.data.user.request.CreateOrderUpdateRequest;
import hcmute.kltn.vtv.model.data.user.response.ListOrderResponse;
import hcmute.kltn.vtv.model.data.user.response.OrderItemResponse;
import hcmute.kltn.vtv.model.data.user.response.OrderResponse;
import hcmute.kltn.vtv.model.extra.OrderStatus;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.service.user.IOrderService;
import hcmute.kltn.vtv.service.user.impl.OrderItemServiceImpl;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer/order")
@RequiredArgsConstructor
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private OrderItemServiceImpl orderItemService;

    @GetMapping("/create")
    public ResponseEntity<OrderResponse> createOrder(@RequestParam List<Long> cartIds,
            HttpServletRequest request) {

        String username = (String) request.getAttribute("username");
        if (cartIds.isEmpty()) {
            throw new NotFoundException("Danh sách mã giỏ hàng không được để trống!");
        }
        return ResponseEntity.ok(orderService.createOrder(username, cartIds));
    }

    @GetMapping("/create-update")
    public ResponseEntity<OrderResponse> createOrderUpdate(CreateOrderUpdateRequest request,
            HttpServletRequest requestHttp) {

        String username = (String) requestHttp.getAttribute("username");
        request.setUsername(username);
        // System.out.println(request);
        request.validate();
        return ResponseEntity.ok(orderService.createOrderUpdate(request));

    }

    @PostMapping("/save")
    public ResponseEntity<OrderResponse> saveOrder(@RequestBody CreateOrderUpdateRequest request,
            HttpServletRequest requestHttp) {

        String username = (String) requestHttp.getAttribute("username");
        request.setUsername(username);
        request.validate();
        return ResponseEntity.ok(orderService.saveOrder(request));
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
    public ResponseEntity<OrderResponse> getOrderDetail(@PathVariable Long orderId,
            HttpServletRequest requestHttp) {
        String username = (String) requestHttp.getAttribute("username");
        return ResponseEntity.ok(orderService.getOrderDetail(username, orderId));
    }

    @PostMapping("/cancel/{orderId}")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long orderId,
            HttpServletRequest requestHttp) {
        String username = (String) requestHttp.getAttribute("username");
        return ResponseEntity.ok(orderService.cancelOrder(username, orderId));
    }

    @GetMapping("/order-item/detail/{orderItemId}")
    public ResponseEntity<OrderItemResponse> getOrderItemByOrderItemId(@PathVariable Long orderItemId,
            HttpServletRequest requestHttp) {
        return ResponseEntity.ok(orderItemService.getOrderItemByOrderItemId(orderItemId));
    }

}
