package hcmute.kltn.vtv.service.vendor;

import hcmute.kltn.vtv.model.data.paging.response.ListProductPageResponse;
import hcmute.kltn.vtv.model.data.vendor.request.ProductRequest;
import hcmute.kltn.vtv.model.data.vendor.response.ListProductResponse;
import hcmute.kltn.vtv.model.data.vendor.response.ProductResponse;
import hcmute.kltn.vtv.model.dto.vtv.ProductDTO;
import hcmute.kltn.vtv.model.entity.vtv.Product;
import hcmute.kltn.vtv.model.extra.Status;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IProductShopService {
    ProductResponse addNewProduct(ProductRequest request);

    ProductResponse getProductDetail(Long productId, String username);

    ListProductResponse getListProductByUsername(String username);

    ListProductPageResponse getListProductByUsernamePage(String username, int page, int size);


    ListProductResponse getListProductShopByCategoryId(Long categoryId, String username);

    ListProductResponse searchProductsByName(String productName, String username);

    ListProductResponse getBestSellingProducts(int limit, String username);

    ListProductResponse getListProductByPriceRange(String username, Long minPrice, Long maxPrice);

    ListProductResponse getListNewProduct(String username);

    ProductResponse updateProduct(ProductRequest productRequest);

    @Transactional
    ProductResponse updateStatusProduct(Long productId, String username, Status status);

    @Transactional
    ProductResponse restoreProductById(Long productId, String username);

    ListProductResponse getAllDeletedProduct(String username);

    ListProductResponse getListProductResponseSort(List<Product> products, String message, boolean isSort);

    ProductDTO getProductToDTO(Product product);
}
