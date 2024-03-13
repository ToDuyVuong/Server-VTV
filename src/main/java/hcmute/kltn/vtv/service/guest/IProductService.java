package hcmute.kltn.vtv.service.guest;

import hcmute.kltn.vtv.model.data.paging.response.ListProductPageResponse;
import hcmute.kltn.vtv.model.data.vendor.response.ListProductResponse;
import hcmute.kltn.vtv.model.data.vendor.response.ProductResponse;
import hcmute.kltn.vtv.model.entity.vendor.Product;

public interface IProductService {
    ProductResponse getProductDetail(Long productId);


    ListProductResponse getListProductByShopId(Long shopId);

    ListProductPageResponse getListProductPageByCategoryId(Long categoryId, int page, int size);

    ListProductResponse getBestSellingProducts(Long shopId, int limit, boolean isShop);

    ListProductResponse getListNewProduct(Long shopId);

    ListProductResponse getListProductByPriceRange(Long shopId, Long minPrice, Long maxPrice);


    Product getProductById(Long productId);
}
