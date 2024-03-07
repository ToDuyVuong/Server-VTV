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
    private IProductService productService;
    @Autowired
    private IFavoriteProductGuestService favoriteProductGuestService;
    @Autowired
    private final IPageService pageService;
    @Autowired
    private IProductPageService productPageService;

    @GetMapping("/detail/{productId}")
    public ResponseEntity<ProductResponse> getProductDetail(@PathVariable Long productId) {
        if (productId == null) {
            throw new NotFoundException("Mã sản phẩm không được để trống!");
        }
        return ResponseEntity.ok(productService.getProductDetail(productId));
    }


    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<ListProductPageResponse> getListProductPageByCategoryId(@RequestParam int page,
                                                                                  @RequestParam int size,
                                                                                  @PathVariable Long categoryId) {
        pageService.checkRequestProductPageParams(page, size);

        return ResponseEntity.ok(productPageService.getListProductPageByCategoryId(categoryId, page, size));
    }

    @GetMapping("/shop/{shopId}")
    public ResponseEntity<ListProductResponse> getListProductByShopId(@PathVariable Long shopId) {
        if (shopId == null) {
            throw new NotFoundException("Mã cửa hàng không được để trống!");
        }
        return ResponseEntity.ok(productService.getListProductByShopId(shopId));
    }

    @GetMapping("/best-selling/{shopId}")
    public ResponseEntity<ListProductResponse> getBestSellingProductsOnShop(@PathVariable Long shopId,
            @RequestParam Long limit) {
        if (shopId == null) {
            throw new NotFoundException("Mã cửa hàng không được để trống!");
        }
        int intLimit = Math.toIntExact(limit);
        if (intLimit <= 0) {
            throw new NotFoundException("Số lượng sản phẩm bán chạy nhất phải lớn hơn 0!");
        }

        return ResponseEntity.ok(productService.getBestSellingProducts(shopId, intLimit, true));
    }

    @GetMapping("/best-selling")
    public ResponseEntity<ListProductResponse> getBestSellingProducts(@RequestParam Long limit) {

        int intLimit = Math.toIntExact(limit);
        if (intLimit <= 0) {
            throw new NotFoundException("Số lượng sản phẩm bán chạy nhất phải lớn hơn 0!");
        }

        return ResponseEntity.ok(productService.getBestSellingProducts(null, intLimit, false));
    }

    @GetMapping("/list-new/{shopId}")
    public ResponseEntity<ListProductResponse> getListNewProductOnShop(@PathVariable Long shopId) {
        if (shopId == null) {
            throw new NotFoundException("Mã cửa hàng không được để trống!");
        }
        return ResponseEntity.ok(productService.getListNewProduct(shopId));
    }

    @GetMapping("/list-new")
    public ResponseEntity<ListProductResponse> getListNewProduct() {
        return ResponseEntity.ok(productService.getListNewProduct(null));
    }

    @GetMapping("/price-range/{shopId}")
    public ResponseEntity<ListProductResponse> getListProductByPriceRangeOnShop(@PathVariable Long shopId,
            @RequestParam Long minPrice,
            @RequestParam Long maxPrice) {
        if (shopId == null) {
            throw new NotFoundException("Mã cửa hàng không được để trống!");
        }
        checkPrice(minPrice, maxPrice);
        return ResponseEntity.ok(productService.getListProductByPriceRange(shopId, minPrice, maxPrice));
    }

    @GetMapping("/price-range")
    public ResponseEntity<ListProductResponse> getListProductByPriceRange(@RequestParam Long minPrice,
            @RequestParam Long maxPrice) {
        checkPrice(minPrice, maxPrice);
        return ResponseEntity.ok(productService.getListProductByPriceRange(null, minPrice, maxPrice));
    }

    @GetMapping("/search/{shopId}")
    public ResponseEntity<ListProductResponse> searchProductsByOnShop(@PathVariable Long shopId,
            @RequestParam String productName) {
        if (shopId == null) {
            throw new NotFoundException("Mã cửa hàng không được để trống!");
        }
        if (productName == null) {
            throw new NotFoundException("Tên sản phẩm không được để trống!");
        }
        return ResponseEntity.ok(productService.searchProducts(shopId, productName));
    }

    @GetMapping("/search")
    public ResponseEntity<ListProductResponse> searchProducts(@RequestParam String productName) {
        if (productName == null) {
            throw new NotFoundException("Tên sản phẩm không được để trống!");
        }
        return ResponseEntity.ok(productService.searchProducts(null, productName));
    }

    public static void checkPrice(@RequestParam Long minPrice, @RequestParam Long maxPrice) {
        if (minPrice == null || maxPrice == null) {
            throw new NotFoundException("Giá sản phẩm không được để trống!");
        }
        if (minPrice <= 0 || maxPrice <= 0) {
            throw new BadRequestException("Giá sản phẩm không được nhỏ hơn hoặc bằng 0!");
        }
        if (minPrice >= maxPrice) {
            throw new BadRequestException("Giá sản phẩm tối thiểu phải nhỏ hơn hoặc bằng giá sản phẩm tối đa!");
        }
    }

    @GetMapping("/count-favorite/{productId}")
    public ResponseEntity<Integer> countFavoriteProduct(@PathVariable Long productId) {
        if (productId == null) {
            throw new NotFoundException("Mã sản phẩm không được để trống!");
        }
        return ResponseEntity.ok(favoriteProductGuestService.countFavoriteProduct(productId));
    }

}
