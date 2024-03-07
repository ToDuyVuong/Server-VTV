package hcmute.kltn.vtv.service.guest.impl;

import hcmute.kltn.vtv.model.data.vendor.response.ListProductResponse;
import hcmute.kltn.vtv.model.data.vendor.response.ProductResponse;
import hcmute.kltn.vtv.model.dto.vtv.ProductDTO;
import hcmute.kltn.vtv.model.entity.vtv.Product;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.vtv.ProductRepository;
import hcmute.kltn.vtv.service.guest.IProductService;
import hcmute.kltn.vtv.service.guest.IReviewService;
import hcmute.kltn.vtv.service.vendor.IProductShopService;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private IProductShopService productShopService;
    @Autowired
    private final IReviewService reviewService;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public ProductResponse getProductDetail(Long productId) {
        Product product = productRepository
                .findById(productId)
                .orElseThrow(() -> new NotFoundException("Sản phẩm không tồn tại!"));

        ProductDTO productDTO = ProductDTO.convertEntityToDTO(product);

        ProductResponse response = new ProductResponse();
        response.setCategoryName(product.getCategory().getName());
        response.setProductDTO(productDTO);
        response.setRating(reviewService.countAverageRatingByProductId(productId));
        response.setShopName(product.getCategory().getShop().getName());
        response.setMessage("Lấy thông tin sản phẩm thành công!");
        response.setStatus("ok");
        response.setCode(200);
        return response;
    }

//    @Override
//    public ListProductResponse getListProductByCategoryId(Long categoryId, boolean isParent) {
//        List<Product> products;
//        if (isParent) {
//            products = productRepository.findByCategoryParentCategoryIdAndStatus(categoryId, Status.ACTIVE)
//                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào trong danh mục cha này!"));
//        } else {
//            products = productRepository.findByCategoryCategoryIdAndStatus(categoryId, Status.ACTIVE)
//                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào trong danh mục này!"));
//        }
//
//        return productShopService.getListProductResponseSort(products,
//                "Lấy danh sách sản phẩm thành công!",
//                true);
//    }

    @Override
    public ListProductResponse getListProductByShopId(Long shopId) {
        List<Product> products = productRepository.findByCategoryShopShopIdAndStatus(shopId, Status.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào trong cửa hàng này!"));

        return productShopService.getListProductResponseSort(products,
                "Lấy danh sách sản phẩm thành công!",
                true);
    }

    @Override
    public ListProductResponse getBestSellingProducts(Long shopId, int limit, boolean isShop) {
        List<Product> products;
        if (isShop) {
            products = productRepository.findByCategoryShopShopIdAndStatusOrderBySoldDescNameAsc(shopId, Status.ACTIVE)
                    .orElseThrow(() -> new NotFoundException("Cửa hàng không có sản phẩm bán chạy!"));
        } else {
            products = productRepository.findByStatusOrderBySoldDescNameAsc(Status.ACTIVE)
                    .orElseThrow(() -> new NotFoundException("Không có sản phẩm bán chạy!"));
        }

        String message = isShop ? "Lấy danh sách sản phẩm bán chạy trong cửa hàng thành công!"
                : "Lấy danh sách sản phẩm bán chạy thành công!";

        return responseWithLimit(products, limit, message);
    }

    private ListProductResponse responseWithLimit(List<Product> products, int limit, String message) {
        ListProductResponse response = productShopService.getListProductResponseSort(products,
                message,
                false);

        if (response.getCount() > limit) {
            response.setProductDTOs(response.getProductDTOs().subList(0, limit));
            response.setCount(limit);
        }

        return response;
    }

    @Override
    public ListProductResponse getListNewProduct(Long shopId) {

        List<Product> products;
        if (shopId == null) {
            Pageable pageable = PageRequest.of(0, 20);
            Page<Product> page = productRepository.findNewestProducts(Status.ACTIVE, pageable)
                    .orElseThrow(() -> new NotFoundException("Không có sản phẩm mới!"));
            products = page.getContent();
        } else {
            products = productRepository.findByCategoryShopShopIdAndStatusOrderByCreateAtDesc(shopId, Status.ACTIVE)
                    .orElseThrow(() -> new NotFoundException("Cửa hàng không có sản phẩm mới!"));
        }

        String message = shopId == null ? "Lấy danh sách sản phẩm mới thành công!"
                : "Lấy danh sách sản phẩm mới trong cửa hàng thành công!";

        return productShopService.getListProductResponseSort(products, message, false);
    }

    @Override
    public ListProductResponse getListProductByPriceRange(Long shopId, Long minPrice, Long maxPrice) {
        List<Product> products;

        if (shopId == null) {
            products = productRepository.findByPriceRange(Status.ACTIVE, minPrice, maxPrice)
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào trong khoảng giá này!"));
        } else {
            products = productRepository.findByPriceRange(shopId, Status.ACTIVE, minPrice, maxPrice)
                    .orElseThrow(() -> new NotFoundException(
                            "Không tìm thấy sản phẩm nào trong cửa hàng có khoảng giá này!"));
        }

        String message = shopId == null ? "Lọc sản phẩm theo giá thành công."
                : "Lọc sản phẩm theo giá trong cửa hàng thành công.";

        return productShopService.getListProductResponseSort(products, message, true);
    }

    @Override
    public ListProductResponse searchProducts(Long shopId, String productName) {
        List<Product> products;

        if (shopId == null) {
            products = productRepository
                    .findAllByNameContainingAndStatus(productName, Status.ACTIVE)
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào có tên tương tự!"));
        } else {
            products = productRepository
                    .findAllByNameContainingAndCategoryShopShopIdAndStatus(productName, shopId, Status.ACTIVE)
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm nào có tên tương tự!"));
        }

        String message = shopId == null ? "Tìm kiếm sản phẩm theo tên thành công!"
                : "Tìm kiếm sản phẩm theo tên trong cửa hàng thành công!";

        return productShopService.getListProductResponseSort(products, message, true);
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository
                .findById(productId)
                .orElseThrow(() -> new NotFoundException("Sản phẩm không tồn tại!"));
    }

}
