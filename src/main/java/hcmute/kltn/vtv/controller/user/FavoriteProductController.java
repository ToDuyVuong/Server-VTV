package hcmute.kltn.vtv.controller.user;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.user.response.FavoriteProductResponse;
import hcmute.kltn.vtv.model.data.user.response.ListFavoriteProductResponse;
import hcmute.kltn.vtv.model.data.guest.ProductResponse;
import hcmute.kltn.vtv.service.user.IFavoriteProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/favorite-product")
@RequiredArgsConstructor
public class FavoriteProductController {

    @Autowired
    private IFavoriteProductService favoriteProductService;

    @PostMapping("/add")
    public ResponseEntity<FavoriteProductResponse> addNewFavoriteProduct(@RequestParam Long productId,
            HttpServletRequest request) {
        if (productId == null) {
            throw new BadRequestException("Mã sản phẩm không được để trống!");
        }
        String username = (String) request.getAttribute("username");

        FavoriteProductResponse response = favoriteProductService.addNewFavoriteProduct(productId, username);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/detail/{favoriteProductId}")
    public ResponseEntity<ProductResponse> getFavoriteProductById(
            @PathVariable("favoriteProductId") Long favoriteProductId,
            HttpServletRequest request) {
        if (favoriteProductId == null) {
            throw new BadRequestException("Mã sản phẩm yêu thích không được để trống!");
        }
        String username = (String) request.getAttribute("username");
        ProductResponse response = favoriteProductService.getProductByFavoriteProductId(favoriteProductId, username);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list")
    public ResponseEntity<ListFavoriteProductResponse> getListFavoriteProduct(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        return ResponseEntity.ok(favoriteProductService.getListFavoriteProduct(username));
    }

    @DeleteMapping("/delete/{favoriteProductId}")
    public ResponseEntity<FavoriteProductResponse> deleteFavoriteProduct(
            @PathVariable("favoriteProductId") Long favoriteProductId,
            HttpServletRequest request) {
        if (favoriteProductId == null) {
            throw new BadRequestException("Mã sản phẩm yêu thích không được để trống!");
        }
        String username = (String) request.getAttribute("username");
        FavoriteProductResponse response = favoriteProductService.deleteFavoriteProduct(favoriteProductId, username);
        return ResponseEntity.ok(response);
    }

}
