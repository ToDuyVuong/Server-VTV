package hcmute.kltn.vtv.controller.guest;

import hcmute.kltn.vtv.model.data.paging.response.ListProductPageResponse;
import hcmute.kltn.vtv.model.data.vendor.response.ListProductResponse;
import hcmute.kltn.vtv.model.data.vendor.response.ProductResponse;
import hcmute.kltn.vtv.service.guest.IFavoriteProductGuestService;
import hcmute.kltn.vtv.service.guest.IPageService;
import hcmute.kltn.vtv.service.guest.IProductPageService;
import hcmute.kltn.vtv.service.guest.IProductService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    @Autowired
    private final IProductService productService;
    @Autowired
    private final IFavoriteProductGuestService favoriteProductGuestService;
    @Autowired
    private final IPageService pageService;


    @GetMapping("/detail/{productId}")
    public ResponseEntity<ProductResponse> getProductDetail(@PathVariable Long productId) {

        return ResponseEntity.ok(productService.getProductDetail(productId));
    }


    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<ListProductPageResponse> getListProductPageByCategoryId(@RequestParam int page,
                                                                                  @RequestParam int size,
                                                                                  @PathVariable Long categoryId) {
        pageService.checkRequestProductPageParams(page, size);

        return ResponseEntity.ok(productService.getListProductPageByCategoryId(categoryId, page, size));
    }

    @GetMapping("/shop/{shopId}")
    public ResponseEntity<ListProductResponse> getListProductByShopId(@PathVariable Long shopId) {

        return ResponseEntity.ok(productService.getListProductByShopId(shopId));
    }

    @GetMapping("/best-selling/{shopId}")
    public ResponseEntity<ListProductResponse> getBestSellingProductsOnShop(@PathVariable Long shopId,
                                                                            @RequestParam int limit) {

        if (limit <= 0) {
            throw new NotFoundException("Số lượng sản phẩm bán chạy nhất phải lớn hơn 0!");
        }

        return ResponseEntity.ok(productService.getBestSellingProducts(shopId, limit, true));
    }

    @GetMapping("/best-selling")
    public ResponseEntity<ListProductResponse> getBestSellingProducts(@RequestParam int limit) {

        if (limit <= 0) {
            throw new NotFoundException("Số lượng sản phẩm bán chạy nhất phải lớn hơn 0!");
        }

        return ResponseEntity.ok(productService.getBestSellingProducts(null, limit, false));
    }

//    @GetMapping("/list-new/{shopId}")
//    public ResponseEntity<ListProductResponse> getListNewProductOnShop(@PathVariable Long shopId) {
//
//        return ResponseEntity.ok(productService.getListNewProduct(shopId));
//    }
//
//    @GetMapping("/list-new")
//    public ResponseEntity<ListProductResponse> getListNewProduct() {
//        return ResponseEntity.ok(productService.getListNewProduct(null));
//    }

//    @GetMapping("/price-range/{shopId}")
//    public ResponseEntity<ListProductResponse> getListProductByPriceRangeOnShop(@PathVariable Long shopId,
//                                                                                @RequestParam Long minPrice,
//                                                                                @RequestParam Long maxPrice) {
//        pageService.checkRequestPriceRangeParams(minPrice, maxPrice);
//        return ResponseEntity.ok(productService.getListProductByPriceRange(shopId, minPrice, maxPrice));
//    }
//
//    @GetMapping("/price-range")
//    public ResponseEntity<ListProductResponse> getListProductByPriceRange(@RequestParam Long minPrice,
//                                                                          @RequestParam Long maxPrice) {
//        pageService.checkRequestPriceRangeParams(minPrice, maxPrice);
//        return ResponseEntity.ok(productService.getListProductByPriceRange(null, minPrice, maxPrice));
//    }


    @GetMapping("/count-favorite/{productId}")
    public ResponseEntity<Integer> countFavoriteProduct(@PathVariable Long productId) {

        return ResponseEntity.ok(favoriteProductGuestService.countFavoriteProduct(productId));
    }

}
