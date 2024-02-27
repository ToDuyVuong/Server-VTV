package hcmute.kltn.vtv.controller.guest;

import hcmute.kltn.vtv.model.data.paging.response.ListProductPageResponse;
import hcmute.kltn.vtv.service.guest.IPageService;
import hcmute.kltn.vtv.service.guest.IProductSuggestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product-suggestion")
@RequiredArgsConstructor
public class ProductSuggestionController {

    @Autowired
    private final IProductSuggestionService productSuggestionService;
    @Autowired
    private final IPageService pageService;

    @GetMapping("/get-page/randomly")
    public ResponseEntity<ListProductPageResponse> getProductSuggestionByRandomly(@Param("page") int page,
                                                                                  @Param("size") int size) {
        pageService.checkRequestProductPageParams(page, size);

        return ResponseEntity.ok(productSuggestionService.suggestByRandomly(page, size));
    }

    @GetMapping("/get-page/randomly/product/{productId}")
    public ResponseEntity<ListProductPageResponse> getProductSuggestionByRandomlyAndProductId(@Param("page") int page,
                                                                                              @Param("size") int size,
                                                                                              @Param("inShop") boolean inShop,
                                                                                              @PathVariable("productId") Long productId) {
        pageService.checkRequestProductPageParams(page, size);

        return ResponseEntity.ok(productSuggestionService.suggestByRandomlyAndProductId(productId, page, size, inShop));
    }


    @GetMapping("/get-page/randomly/category/{categoryId}")
    public ResponseEntity<ListProductPageResponse> getProductSuggestionByCategory(@Param("page") int page,
                                                                                  @Param("size") int size,
                                                                                  @PathVariable("categoryId") Long categoryId) {
        pageService.checkRequestProductPageParams(page, size);

        return ResponseEntity.ok(productSuggestionService.suggestByCategoryAndRandomly(categoryId, page, size));
    }


}
