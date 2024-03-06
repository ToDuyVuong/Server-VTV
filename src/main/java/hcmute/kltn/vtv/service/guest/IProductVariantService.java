package hcmute.kltn.vtv.service.guest;

import hcmute.kltn.vtv.model.entity.vtv.ProductVariant;

public interface IProductVariantService {
    ProductVariant checkAndProductVariantAvailableWithQuantity(Long productVariantId, int quantity);
}
