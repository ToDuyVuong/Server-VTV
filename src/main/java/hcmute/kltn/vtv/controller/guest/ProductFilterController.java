package hcmute.kltn.vtv.controller.guest;


import hcmute.kltn.vtv.model.data.paging.response.ListProductPageResponse;
import hcmute.kltn.vtv.service.guest.IPageService;
import hcmute.kltn.vtv.service.guest.IProductFilterService;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product-filter")
public class ProductFilterController {


    @Autowired
    private final IProductFilterService productFilterService;
    @Autowired
    private final IPageService pageService;


    @GetMapping("/{filter}")
    public ResponseEntity<ListProductPageResponse> getFilterProductPage(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @PathVariable String filter) {

        if (filter == null) {
            throw new NotFoundException("Loại lọc không được để trống!");
        }
        if (!filter.equals("newest") && !filter.equals("best-selling") && !filter.equals("price-asc") &&
                !filter.equals("price-desc") && !filter.equals("random")) {
            throw new NotFoundException("Loại lọc không hợp lệ! Loại lọc phải là newest, best-selling, price-asc, price-desc hoặc random.");
        }
        pageService.checkRequestProductPageParams(page, size);

        return ResponseEntity.ok(productFilterService.getFilterProductPage(page, size, filter));
    }


    @GetMapping("/price-range/{filter}")
    public ResponseEntity<ListProductPageResponse> getFilterProductPageByPriceRange(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "minPrice") Long minPrice,
            @RequestParam(value = "maxPrice") Long maxPrice,
            @PathVariable String filter) {

        if (filter == null) {
            throw new NotFoundException("Loại lọc không được để trống!");
        }
        if (!filter.equals("newest") && !filter.equals("best-selling") && !filter.equals("price-asc") &&
                !filter.equals("price-desc") && !filter.equals("random")) {
            throw new NotFoundException("Loại lọc không hợp lệ! Loại lọc phải là newest, best-selling, price-asc, price-desc hoặc random.");
        }
        pageService.checkRequestProductPageParams(page, size);
        pageService.checkRequestPriceRangeParams(minPrice, maxPrice);

        return ResponseEntity.ok(productFilterService.getFilterProductPageByPriceRange(page, size, filter, minPrice, maxPrice));
    }



}



