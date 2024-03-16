package hcmute.kltn.vtv.service.vendor;

import hcmute.kltn.vtv.model.data.vendor.request.ProductVariantRequest;
import hcmute.kltn.vtv.model.entity.vendor.Product;
import hcmute.kltn.vtv.model.entity.vendor.ProductVariant;
import hcmute.kltn.vtv.model.extra.Status;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IProductVariantShopService {
    @Transactional
    List<ProductVariant> addNewProductVariants(List<ProductVariantRequest> requests, Product product);

    @Transactional
    ProductVariant addNewProductVariant(ProductVariantRequest request, Product product);

    List<ProductVariant> updateProductVariants(List<ProductVariantRequest> requests, Product product);

    @Transactional
    ProductVariant updateProductVariant(ProductVariantRequest request, Product product);

    @Transactional
    List<ProductVariant> updateStatusProductVariants(Long productId, List<ProductVariant> productVariants, Status status);

//    List<ProductVariant> addNewListProductVariant(List<ProductVariantRequest> productVariantRequests, Long shopId);
//
//    List<ProductVariant> getListProductVariant(List<ProductVariantRequest> requests, Long shopId, Long productId);
}
