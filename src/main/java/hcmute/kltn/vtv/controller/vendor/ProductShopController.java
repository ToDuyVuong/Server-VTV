package hcmute.kltn.vtv.controller.vendor;

import hcmute.kltn.vtv.model.data.paging.response.ListProductPageResponse;
import hcmute.kltn.vtv.model.data.vendor.request.ProductRequest;
import hcmute.kltn.vtv.model.data.vendor.response.ProductResponse;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.service.guest.IPageService;

import hcmute.kltn.vtv.service.vendor.IProductShopService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendor/product")
@RequiredArgsConstructor
public class ProductShopController {

    @Autowired
    private final IProductShopService productShopService;
    @Autowired
    private final IPageService pageService;



    @PostMapping("/add")
    public ResponseEntity<ProductResponse> addNewProduct(@ModelAttribute ProductRequest request,
                                                         HttpServletRequest httpServletRequest) {
        String username = (String) httpServletRequest.getAttribute("username");
        request.validate();

        return ResponseEntity.ok(productShopService.addNewProduct(request, username));
    }


    @PostMapping("/update/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long productId,
                                                         @ModelAttribute ProductRequest request,
                                                         HttpServletRequest httpServletRequest) {
        String username = (String) httpServletRequest.getAttribute("username");
        request.setProductId(productId);
        request.validate();
        return ResponseEntity.ok(productShopService.updateProduct(request,  username));
    }


    @PatchMapping("/update/{productId}/status/{status}")
    public ResponseEntity<ProductResponse> updateStatusProduct(@PathVariable Long productId,
                                                               @PathVariable Status status,
                                                               HttpServletRequest httpServletRequest) {
        String username = (String) httpServletRequest.getAttribute("username");
        return ResponseEntity.ok(productShopService.updateStatusProduct(productId, username, status));
    }


    @PatchMapping("/restore/{productId}")
    public ResponseEntity<ProductResponse> restoreProductById(@PathVariable Long productId,
                                                              HttpServletRequest httpServletRequest) {
        String username = (String) httpServletRequest.getAttribute("username");
        return ResponseEntity.ok(productShopService.restoreProductById(productId, username));
    }



    @GetMapping("/page/status/{status}")
    public ResponseEntity<ListProductPageResponse> getPageProductByStatus(@RequestParam int page,
                                                                          @RequestParam int size,
                                                                          @PathVariable Status status,
                                                                          HttpServletRequest httpServletRequest) {
        pageService.checkRequestProductPageParams(page, size);
        String username = (String) httpServletRequest.getAttribute("username");
        return ResponseEntity.ok(productShopService.getListProductPageByUsernameAndStatus(username, page, size, status));
    }











//    @PostMapping("/add")
//    public ResponseEntity<ProductResponse> addNewProduct(@RequestBody ProductRequest request,
//                                                         HttpServletRequest httpServletRequest) {
//        String username = (String) httpServletRequest.getAttribute("username");
//        request.setUsername(username);
//        request.validate();
//        return ResponseEntity.ok(productService.addNewProduct(request));
//    }
//
//
//    @GetMapping("/detail/{productId}")
//    public ResponseEntity<ProductResponse> getProductDetail(@PathVariable Long productId,
//                                                            HttpServletRequest httpServletRequest) {
//
//        String username = (String) httpServletRequest.getAttribute("username");
//        return ResponseEntity.ok(productService.getProductDetail(productId, username));
//    }
//
//    @GetMapping("/page")
//    public ResponseEntity<ListProductPageResponse> getPageProductByUsername(@RequestParam int page,
//                                                                            @RequestParam int size,
//                                                                            HttpServletRequest httpServletRequest) {
//        pageService.checkRequestProductPageParams(page, size);
//        String username = (String) httpServletRequest.getAttribute("username");
//        return ResponseEntity.ok(productService.getListProductByUsernamePage(username, page, size));
//    }
//
//    @GetMapping("/list")
//    public ResponseEntity<ListProductResponse> getListProductByUsername(HttpServletRequest httpServletRequest) {
//        String username = (String) httpServletRequest.getAttribute("username");
//        return ResponseEntity.ok(productService.getListProductByUsername(username));
//    }
//
//    @GetMapping("/list/category/{categoryId}")
//    public ResponseEntity<ListProductResponse> getListProductShopByCategoryId(@PathVariable Long categoryId,
//                                                                              HttpServletRequest httpServletRequest) {
//
//        String username = (String) httpServletRequest.getAttribute("username");
//        return ResponseEntity.ok(productService.getListProductShopByCategoryId(categoryId, username));
//    }
//
//    @GetMapping("/search")
//    public ResponseEntity<ListProductResponse> searchProductsByName(@RequestParam String productName,
//                                                                    HttpServletRequest httpServletRequest) {
//        if (productName == null) {
//            throw new NotFoundException("Tên sản phẩm không được để trống!");
//        }
//        String username = (String) httpServletRequest.getAttribute("username");
//        return ResponseEntity.ok(productService.searchProductsByName(productName, username));
//    }
//
//    @GetMapping("/best-selling")
//    public ResponseEntity<ListProductResponse> getBestSellingProducts(@RequestParam int limit,
//                                                                      HttpServletRequest httpServletRequest) {
//        if (limit <= 0) {
//            throw new BadRequestException("Số lượng sản phẩm bán chạy nhất phải lớn hơn 0!");
//        }
//        String username = (String) httpServletRequest.getAttribute("username");
//        return ResponseEntity.ok(productService.getBestSellingProducts(limit, username));
//    }
//
//    @GetMapping("/price-range")
//    public ResponseEntity<ListProductResponse> getListProductByPriceRange(@RequestParam Long minPrice,
//                                                                          @RequestParam Long maxPrice,
//                                                                          HttpServletRequest httpServletRequest) {
//
//        pageService.checkRequestPriceRangeParams(minPrice, maxPrice);
//
//        String username = (String) httpServletRequest.getAttribute("username");
//        return ResponseEntity.ok(productService.getListProductByPriceRange(username, minPrice, maxPrice));
//    }
//
//
//    @GetMapping("/list-new")
//    public ResponseEntity<ListProductResponse> getListNewProduct(HttpServletRequest httpServletRequest) {
//        String username = (String) httpServletRequest.getAttribute("username");
//        return ResponseEntity.ok(productService.getListNewProduct(username));
//    }
//
//    @PutMapping("/update/{productId}")
//    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long productId,
//                                                         @RequestBody ProductRequest request,
//                                                         HttpServletRequest httpServletRequest) {
//
//        String username = (String) httpServletRequest.getAttribute("username");
//        request.setUsername(username);
//        request.setProductId(productId);
//        request.validateUpdate();
//        return ResponseEntity.ok(productService.updateProduct(request));
//    }
//
//    @PatchMapping("/update/status/{productId}")
//    public ResponseEntity<ProductResponse> updateStatusProduct(@PathVariable Long productId,
//                                                               @RequestParam Status status,
//                                                               HttpServletRequest httpServletRequest) {
//
//        if (status == null) {
//            throw new NotFoundException("Trạng thái sản phẩm không được để trống!");
//        }
//
//        String username = (String) httpServletRequest.getAttribute("username");
//        return ResponseEntity.ok(productService.updateStatusProduct(productId, username, status));
//    }
//
//    @GetMapping("/all-deleted")
//    public ResponseEntity<ListProductResponse> getAllDeletedProduct(HttpServletRequest httpServletRequest) {
//        String username = (String) httpServletRequest.getAttribute("username");
//        return ResponseEntity.ok(productService.getAllDeletedProduct(username));
//    }
//
//    @PutMapping("/restore/{productId}")
//    public ResponseEntity<ProductResponse> restoreProductById(@PathVariable Long productId,
//                                                              HttpServletRequest httpServletRequest) {
//
//        String username = (String) httpServletRequest.getAttribute("username");
//        return ResponseEntity.ok(productService.restoreProductById(productId, username));
//    }

}
