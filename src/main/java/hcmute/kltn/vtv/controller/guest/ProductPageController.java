package hcmute.kltn.vtv.controller.guest;

import hcmute.kltn.vtv.model.data.paging.response.ListProductPageResponse;
import hcmute.kltn.vtv.service.guest.IPageService;
import hcmute.kltn.vtv.service.guest.IProductPageService;
import hcmute.kltn.vtv.service.guest.IProductService;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product/page")
@RequiredArgsConstructor
public class ProductPageController {

    @Autowired
    private IProductPageService productPageService;

    @Autowired
    private final IPageService pageService;

    @Autowired
    private final IProductService productService;

    @GetMapping("/list")
    public ResponseEntity<ListProductPageResponse> getListProductPage(@RequestParam int page,
            @RequestParam int size) {
        pageService.checkRequestProductPageParams(page, size);

        return ResponseEntity.ok(productPageService.getListProductPage(page, size));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ListProductPageResponse> getListProductPageByCategoryId(@RequestParam int page,
            @RequestParam int size,
            @PathVariable Long categoryId) {
        pageService.checkRequestProductPageParams(page, size);

        return ResponseEntity.ok(productService.getListProductPageByCategoryId(categoryId, page, size));
    }

    @GetMapping("/shop/{shopId}")
    public ResponseEntity<ListProductPageResponse> getListProductPageByShopId(@RequestParam int page,
            @RequestParam int size,
            @PathVariable Long shopId) {
        if (shopId == null) {
            throw new NotFoundException("Mã cửa hàng không được để trống!");
        }
        pageService.checkRequestProductPageParams(page, size);

        return ResponseEntity.ok(productPageService.getListProductsPageByShopId(shopId, page, size));
    }

//    @GetMapping("/shop/{shopId}/best-selling")
//    public ResponseEntity<ListProductPageResponse> getListBestSellingProductsPageByShopId(@RequestParam int page,
//            @RequestParam int size,
//            @PathVariable Long shopId) {
//        if (shopId == null) {
//            throw new NotFoundException("Mã cửa hàng không được để trống!");
//        }
//        pageService.checkRequestProductPageParams(page, size);
//
//        return ResponseEntity.ok(productPageService.getListBestSellingProductsPageByShopId(shopId, page, size));
//    }

    @GetMapping("/shop/{shopId}/new")
    public ResponseEntity<ListProductPageResponse> getListNewProductsPageByShopId(@RequestParam int page,
            @RequestParam int size,
            @PathVariable Long shopId) {
        if (shopId == null) {
            throw new NotFoundException("Mã cửa hàng không được để trống!");
        }
        pageService.checkRequestProductPageParams(page, size);

        return ResponseEntity.ok(productPageService.getListNewProductsPageByShopId(shopId, page, size));
    }

//    @GetMapping("/price-range")
//    public ResponseEntity<ListProductPageResponse> getListProductsPagePriceRange(@RequestParam int page,
//            @RequestParam int size,
//            @RequestParam Long minPrice,
//            @RequestParam Long maxPrice) {
//        pageService.checkRequestProductPageParams(page, size);
//        pageService.checkRequestPriceRangeParams(minPrice, maxPrice);
//
//        return ResponseEntity.ok(productPageService.getListProductsPagePriceRange(minPrice, maxPrice, page, size));
//    }
//
//    @GetMapping("/shop/{shopId}/price-range")
//    public ResponseEntity<ListProductPageResponse> getListProductsPageByShopAndPriceRange(@RequestParam int page,
//            @RequestParam int size,
//            @PathVariable Long shopId,
//            @RequestParam Long minPrice,
//            @RequestParam Long maxPrice) {
//        if (shopId == null) {
//            throw new NotFoundException("Mã cửa hàng không được để trống!");
//        }
//        pageService.checkRequestProductPageParams(page, size);
//        pageService.checkRequestPriceRangeParams(minPrice, maxPrice);
//
//        return ResponseEntity
//                .ok(productPageService.getListProductsPageByShopAndPriceRange(shopId, minPrice, maxPrice, page, size));
//    }
//
//    @GetMapping("/shop/{shopId}/price-range/sort")
//    public ResponseEntity<ListProductPageResponse> getListProductsPageByShopAndPriceRangeSort(@RequestParam int page,
//            @RequestParam int size,
//            @PathVariable Long shopId,
//            @RequestParam Long minPrice,
//            @RequestParam Long maxPrice,
//            @RequestParam String sort) {
//        if (shopId == null) {
//            throw new NotFoundException("Mã cửa hàng không được để trống!");
//        }
//        pageService.checkRequestProductPageParams(page, size);
//        pageService.checkRequestPriceRangeParams(minPrice, maxPrice);
//        pageService.checkRequestSortParams(sort);
//
//        return ResponseEntity.ok(productPageService.getListProductsPageByShopAndPriceRangeAndSort(shopId, minPrice,
//                maxPrice, page, size, sort));
//    }
//
//    @GetMapping("/search/sort")
//    public ResponseEntity<ListProductPageResponse> getListProductsPageBySearchSort(
//            @RequestParam int page,
//            @RequestParam int size,
//            @RequestParam String search,
//            @RequestParam String sort) {
//        pageService.checkRequestProductPageParams(page, size);
//        pageService.checkRequestSortParams(sort);
//        if (search == null) {
//            throw new NotFoundException("Từ khóa tìm kiếm không được để trống!");
//        }
//
//        return ResponseEntity.ok(productPageService.getListProductsPageByShopSearchSort(search, page, size, sort));
//    }
//
//    @GetMapping("/search/price/sort")
//    public ResponseEntity<ListProductPageResponse> getListProductsPageByShopSearchPriceSort(@RequestParam int page,
//            @RequestParam int size,
//            @RequestParam String search,
//            @RequestParam String sort,
//            @RequestParam Long minPrice,
//            @RequestParam Long maxPrice) {
//        pageService.checkRequestProductPageParams(page, size);
//        pageService.checkRequestSortParams(sort);
//        if (search == null) {
//            throw new NotFoundException("Từ khóa tìm kiếm không được để trống!");
//        }
//
//        return ResponseEntity.ok(productPageService.getListProductsPageBySearchAndPriceSort(search, minPrice, maxPrice,
//                page, size, sort));
//    }

}
