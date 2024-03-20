package hcmute.kltn.vtv.service.guest.impl;

import hcmute.kltn.vtv.model.data.paging.response.ListProductPageResponse;
import hcmute.kltn.vtv.model.data.vendor.response.ListProductResponse;
import hcmute.kltn.vtv.model.data.vendor.response.ProductResponse;
import hcmute.kltn.vtv.model.entity.vendor.Product;
import hcmute.kltn.vtv.model.entity.vtv.Category;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.vendor.ProductRepository;
import hcmute.kltn.vtv.repository.vtv.CategoryRepository;
import hcmute.kltn.vtv.service.guest.IProductService;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public ProductResponse getProductDetail(Long productId) {
        checkExistProductById(productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Sản phẩm không tồn tại!"));


        return ProductResponse.productResponse(product, "Lấy thông tin sản phẩm thành công!", "OK");
    }


    @Override
    public void updateProductSold(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Sản phẩm không tồn tại!"));
        product.setSold(product.getSold() - quantity);
        try {
            productRepository.save(product);
        }catch (Exception e){
            throw new NotFoundException("Cập nhật số lượng sản phẩm bán thất bại!");
        }
    }


    @Override
    public ListProductResponse getListProductByShopId(Long shopId) {
        List<Product> products = productRepository.findByShopShopIdAndStatus(shopId, Status.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào trong cửa hàng này!"));

        return ListProductResponse.listProductResponse(products, "Lấy danh sách sản phẩm theo cửa hàng thành công!", "OK");
    }


    @Override
    public ListProductPageResponse getListProductPageByCategoryId(Long categoryId, int page, int size) {
        if (checkExistCategoryHasChild(categoryId)) {
            List<Product> products = getProductsByCategoryAndDescendants(categoryId);
            Page<Product> productPage = new PageImpl<>(products, PageRequest.of(page - 1, size), products.size());
            return ListProductPageResponse.listProductPageResponse(productPage, size,
                    "Lấy danh sách sản phẩm theo danh mục thành công!");
        } else {
            return getProductsByCategoryId(categoryId, page, size);
        }
    }

    private ListProductPageResponse getProductsByCategoryId(Long categoryId, int page, int size) {
        Page<Product> productPage = productRepository
                .findAllByCategoryCategoryIdAndStatus(categoryId, Status.ACTIVE, PageRequest.of(page - 1, size))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào trong danh mục này!"));

        String message = "Lấy danh sách sản phẩm theo danh mục thành công!";

        return ListProductPageResponse.listProductPageResponse(productPage, size, message);
    }

    private List<Product> getProductsByCategoryAndDescendants(Long categoryId) {
        List<Category> childCategories = categoryRepository.findAllByParent_CategoryIdAndStatus(categoryId, Status.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục con nào trong danh mục này!"));

        List<Product> products = new ArrayList<>();
        for (Category childCategory : childCategories) {
            List<Product> categoryProducts;
            if (checkExistCategoryHasChild(childCategory.getCategoryId())) {
                categoryProducts = getProductsByCategoryAndDescendants(childCategory.getCategoryId());
                products.addAll(categoryProducts);
            } else {
                categoryProducts = productRepository
                        .findAllByCategoryCategoryIdAndStatus(childCategory.getCategoryId(), Status.ACTIVE)
                        .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào trong danh mục này!"));
                products.addAll(categoryProducts);
            }
        }

        return products;
    }

    private boolean checkExistCategoryHasChild(Long categoryId) {
        return categoryRepository.existsByParentCategoryIdAndStatus(categoryId, Status.ACTIVE);
    }

    @Override
    public ListProductResponse getBestSellingProducts(Long shopId, int limit, boolean isShop) {
        List<Product> products;
        if (isShop) {
            products = productRepository.findByShopShopIdAndStatusOrderBySoldDescNameAsc(shopId, Status.ACTIVE)
                    .orElseThrow(() -> new NotFoundException("Cửa hàng không có sản phẩm bán chạy!"));
        } else {
            products = productRepository.findByStatusOrderBySoldDescNameAsc(Status.ACTIVE)
                    .orElseThrow(() -> new NotFoundException("Không có sản phẩm bán chạy!"));
        }

        String message = isShop ? "Lấy danh sách sản phẩm bán chạy trong cửa hàng thành công!"
                : "Lấy danh sách sản phẩm bán chạy thành công!";

        return ListProductResponse.listProductResponse(products, message, "OK");
    }


//    @Override
//    public ListProductResponse getListNewProduct(Long shopId) {
//
//        List<Product> products;
//        if (shopId == null) {
//            Pageable pageable = PageRequest.of(0, 20);
//            Page<Product> page = productRepository.findNewestProducts(Status.ACTIVE, pageable)
//                    .orElseThrow(() -> new NotFoundException("Không có sản phẩm mới!"));
//            products = page.getContent();
//        } else {
//            products = productRepository.findByShopShopIdAndStatusOrderByCreateAtDesc(shopId, Status.ACTIVE)
//                    .orElseThrow(() -> new NotFoundException("Cửa hàng không có sản phẩm mới!"));
//        }
//
//        String message = shopId == null ? "Lấy danh sách sản phẩm mới thành công!"
//                : "Lấy danh sách sản phẩm mới trong cửa hàng thành công!";
//
//        return productShopService.getListProductResponseSort(products, message, false);
//    }
//
//    @Override
//    public ListProductResponse getListProductByPriceRange(Long shopId, Long minPrice, Long maxPrice) {
//        List<Product> products;
//
//        if (shopId == null) {
//            products = productRepository.findByPriceRange(Status.ACTIVE, minPrice, maxPrice)
//                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào trong khoảng giá này!"));
//        } else {
//            products = productRepository.findByPriceRange(shopId, Status.ACTIVE, minPrice, maxPrice)
//                    .orElseThrow(() -> new NotFoundException(
//                            "Không tìm thấy sản phẩm nào trong cửa hàng có khoảng giá này!"));
//        }
//
//        String message = shopId == null ? "Lọc sản phẩm theo giá thành công."
//                : "Lọc sản phẩm theo giá trong cửa hàng thành công.";
//
//        return productShopService.getListProductResponseSort(products, message, true);
//    }


    @Override
    public Product getProductById(Long productId) {
        return productRepository
                .findById(productId)
                .orElseThrow(() -> new NotFoundException("Sản phẩm không tồn tại!"));
    }


    private void checkExistProductById(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new NotFoundException("Sản phẩm không tồn tại!");
        }
    }

}
