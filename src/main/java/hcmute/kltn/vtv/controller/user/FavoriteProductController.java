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

    private final IFavoriteProductService favoriteProductService;

    @PostMapping("/add/{productId}")
    public ResponseEntity<FavoriteProductResponse> addNewFavoriteProduct(@PathVariable Long productId,
                                                                         HttpServletRequest request) {
        String username = (String) request.getAttribute("username");

        return ResponseEntity.ok(favoriteProductService.addNewFavoriteProduct(productId, username));
    }


    @GetMapping("/detail/{favoriteProductId}")
    public ResponseEntity<ProductResponse> getProductByFavoriteProductId(
                         @PathVariable("favoriteProductId") Long favoriteProductId,
                         HttpServletRequest request) {
        String username = (String) request.getAttribute("username");

        return ResponseEntity.ok(favoriteProductService.getProductByFavoriteProductId(favoriteProductId, username));
    }


    @GetMapping("/list")
    public ResponseEntity<ListFavoriteProductResponse> getListFavoriteProduct(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");

        return ResponseEntity.ok(favoriteProductService.getListFavoriteProduct(username));
    }


    @DeleteMapping("/delete/{favoriteProductId}")
    public ResponseEntity<FavoriteProductResponse> deleteFavoriteProductById(
            @PathVariable("favoriteProductId") Long favoriteProductId,
            HttpServletRequest request) {
        String username = (String) request.getAttribute("username");

        return ResponseEntity.ok(favoriteProductService.deleteFavoriteProduct(favoriteProductId, username));
    }

}
