package hcmute.kltn.vtv.controller.user;

import hcmute.kltn.vtv.model.data.user.request.CartRequest;
import hcmute.kltn.vtv.model.data.user.response.CartResponse;
import hcmute.kltn.vtv.model.data.user.response.ListCartResponse;
import hcmute.kltn.vtv.service.user.ICartService;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customer/cart")
@RequiredArgsConstructor
public class CartController {

    @Autowired
    private final ICartService cartService;

    @PostMapping("/add")
    public ResponseEntity<CartResponse> addNewCart(@RequestBody CartRequest cartRequest, HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        cartRequest.setUsername(username);
        cartRequest.validate();

        return ResponseEntity.ok(cartService.addNewCart(cartRequest));
    }

    @PutMapping("/update")
    public ResponseEntity<CartResponse> updateCart(@RequestBody CartRequest cartRequest, HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        cartRequest.setUsername(username);
        cartRequest.validateUpdate();

        return ResponseEntity.ok(cartService.updateCart(cartRequest));
    }

    @DeleteMapping("/delete/{cartId}")
    public ResponseEntity<CartResponse> deleteCart(@PathVariable UUID cartId, HttpServletRequest request) {

        String username = (String) request.getAttribute("username");
        return ResponseEntity.ok(cartService.deleteCart(cartId, username));
    }

    @GetMapping("/get-list")
    public ResponseEntity<ListCartResponse> getListCartByUsername(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        return ResponseEntity.ok(cartService.getListCartByUsername(username));
    }

    @GetMapping("/get-list-by-list-cart-id")
    public ResponseEntity<ListCartResponse> getListCartByUsernameAndListCartId(@RequestParam List<UUID> cartIds,
            HttpServletRequest request) {
        if (cartIds == null || cartIds.isEmpty()) {
            throw new NotFoundException("Danh sách mã giỏ hàng không hợp lệ!");
        }
        String username = (String) request.getAttribute("username");

        return ResponseEntity.ok(cartService.getListCartByUsernameAndListCartId(username, cartIds));
    }

    @DeleteMapping("/delete-by-shop-id/{shopId}")
    public ResponseEntity<ListCartResponse> deleteCartByShopId(@PathVariable Long shopId, HttpServletRequest request) {
        if (shopId == null || shopId <= 0) {
            throw new NotFoundException("Mã cửa hàng không hợp lệ!");
        }
        String username = (String) request.getAttribute("username");
        return ResponseEntity.ok(cartService.deleteCartByShopId(shopId, username));
    }

}
