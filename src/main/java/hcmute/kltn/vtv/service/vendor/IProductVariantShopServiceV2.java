package hcmute.kltn.vtv.service.vendor;

import hcmute.kltn.vtv.model.data.vendor.request.ProductVariantRequest;
import hcmute.kltn.vtv.model.data.vendor.request.ProductVariantRequestV2;
import hcmute.kltn.vtv.model.entity.vendor.ProductVariant;

import java.util.List;

public interface IProductVariantShopServiceV2 {

    List<ProductVariant> addNewListProductVariant(List<ProductVariantRequestV2> productVariantRequests, Long shopId);

    List<ProductVariant> getListProductVariant(List<ProductVariantRequestV2> requests, Long shopId, Long productId);
}
