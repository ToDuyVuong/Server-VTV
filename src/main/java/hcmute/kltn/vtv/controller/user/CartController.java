package hcmute.kltn.vtv.controller.user;

import hcmute.kltn.vtv.model.data.guest.ResponseClass;
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
        cartRequest.validate();

        return ResponseEntity.ok(cartService.addNewCart(cartRequest, username));
    }

    @PutMapping("/update/{cartId}")
    public ResponseEntity<CartResponse> updateCart(@PathVariable UUID cartId,
                                                   @RequestParam int quantity,
                                                   HttpServletRequest request) {
        String username = (String) request.getAttribute("username");

        if (quantity == 0) {
            throw new NotFoundException("Số lượng sản phẩm không hợp lệ! Cần tăng hoặc giảm số lượng sản phẩm.");
        }

        return ResponseEntity.ok(cartService.updateCart(cartId, quantity, username));
    }

    @DeleteMapping("/delete/{cartId}")
    public ResponseEntity<CartResponse> deleteCart(@PathVariable UUID cartId, HttpServletRequest request) {

        String username = (String) request.getAttribute("username");
        return ResponseEntity.ok(cartService.deleteCartById(cartId, username));
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
