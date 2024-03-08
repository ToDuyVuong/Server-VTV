package hcmute.kltn.vtv.service.guest;

import hcmute.kltn.vtv.model.entity.vtv.ProductVariant;
import hcmute.kltn.vtv.model.extra.Status;

import java.util.List;

public interface IProductVariantService {
    ProductVariant checkAndProductVariantAvailableWithQuantity(Long productVariantId, int quantity);

    List<ProductVariant> filterProductVariantsByStatus(List<ProductVariant> productVariants, Status status);
}
