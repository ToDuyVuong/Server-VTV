package hcmute.kltn.vtv.service.vendor;

import hcmute.kltn.vtv.model.data.vendor.request.ProductVariantRequest;
import hcmute.kltn.vtv.model.entity.vendor.ProductVariant;

import java.util.List;

public interface IProductVariantShopService {

    List<ProductVariant> addNewListProductVariant(List<ProductVariantRequest> productVariantRequests, Long shopId);

    List<ProductVariant> getListProductVariant(List<ProductVariantRequest> requests, Long shopId, Long productId);
}
