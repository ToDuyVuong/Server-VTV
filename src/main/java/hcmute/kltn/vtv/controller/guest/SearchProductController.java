package hcmute.kltn.vtv.controller.guest;

import hcmute.kltn.vtv.model.data.paging.response.ListProductPageResponse;
import hcmute.kltn.vtv.service.guest.IPageService;
import hcmute.kltn.vtv.service.user.ISearchHistoryService;
import hcmute.kltn.vtv.service.user.ISearchProductService;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search/product")
@RequiredArgsConstructor
public class SearchProductController {

    @Autowired
    private ISearchProductService searchProductService;

    @Autowired
    private ISearchHistoryService searchHistoryService;

    @Autowired
    private final IPageService pageService;



    @GetMapping("/sort")
    public ResponseEntity<ListProductPageResponse> getListProductsPageBySearchSort(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String search,
            @RequestParam String sort,
            @AuthenticationPrincipal UserDetails userDetails) {


        pageService.checkRequestProductPageParams(page, size);
        pageService.checkRequestSortParams(sort);

        if (search == null) {
            throw new NotFoundException("Từ khóa tìm kiếm không được để trống!");
        }

        if (userDetails != null) {
            String username = userDetails.getUsername();
            searchHistoryService.addNewSearchHistory(username, search);
        }

        return ResponseEntity.ok(searchProductService.getProductsPageBySearchAndSort(search, page, size, sort));
    }




    @GetMapping("/price-range/sort")
    public ResponseEntity<ListProductPageResponse> getListProductsPageBySearchAndPriceRangeAndSort(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String search,
            @RequestParam String sort,
            @RequestParam Long minPrice,
            @RequestParam Long maxPrice,
            @AuthenticationPrincipal UserDetails userDetails) {


        pageService.checkRequestProductPageParams(page, size);
        pageService.checkRequestSortParams(sort);
        pageService.checkRequestPriceRangeParams(minPrice, maxPrice);

        if (search == null) {
            throw new NotFoundException("Từ khóa tìm kiếm không được để trống!");
        }

        if (userDetails != null) {
            String username = userDetails.getUsername();
            searchHistoryService.addNewSearchHistory(username, search);
        }

        return ResponseEntity.ok(searchProductService.getProductsPageBySearchAndPriceRangeAndSort(search, page, size, sort, minPrice, maxPrice));
    }




    @GetMapping("/shop/{shopId}/sort")
    public ResponseEntity<ListProductPageResponse> getListProductsPageBySearchSortOnShop(
            @PathVariable Long shopId,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String search,
            @RequestParam String sort,
            @AuthenticationPrincipal UserDetails userDetails) {


        pageService.checkRequestProductPageParams(page, size);
        pageService.checkRequestSortParams(sort);

        if (search == null) {
            throw new NotFoundException("Từ khóa tìm kiếm không được để trống!");
        }

        if (userDetails != null) {
            String username = userDetails.getUsername();
            searchHistoryService.addNewSearchHistory(username, search);
        }

        return ResponseEntity.ok(searchProductService.getProductsPageBySearchAndSortOnShop(search, shopId, page, size, sort));
    }


    @GetMapping("/shop/{shopId}/price-range/sort")
    public ResponseEntity<ListProductPageResponse> getProductsPageBySearchAndPriceRangeAndSortOnShop(
            @PathVariable Long shopId,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String search,
            @RequestParam String sort,
            @RequestParam Long minPrice,
            @RequestParam Long maxPrice,
            @AuthenticationPrincipal UserDetails userDetails) {


        pageService.checkRequestProductPageParams(page, size);
        pageService.checkRequestPriceRangeParams(minPrice, maxPrice);
        pageService.checkRequestSortParams(sort);

        if (search == null) {
            throw new NotFoundException("Từ khóa tìm kiếm không được để trống!");
        }

        if (userDetails != null) {
            String username = userDetails.getUsername();
            searchHistoryService.addNewSearchHistory(username, search);
        }

        return ResponseEntity.ok(searchProductService
                .getProductsPageBySearchAndPriceRangeAndSortOnShop(
                        search, shopId, page, size, sort, minPrice, maxPrice));
    }



//    @GetMapping("/price/sort")
//    public ResponseEntity<ListProductPageResponse> getListProductsPageByShopSearchPriceSort(@RequestParam int page,
//                                                                                            @RequestParam int size,
//                                                                                            @RequestParam String search,
//                                                                                            @RequestParam String sort,
//                                                                                            @RequestParam Long minPrice,
//                                                                                            @RequestParam Long maxPrice) {
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
