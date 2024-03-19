package hcmute.kltn.vtv.service.guest;

import hcmute.kltn.vtv.model.entity.vendor.ProductVariant;
import hcmute.kltn.vtv.model.extra.Status;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IProductVariantService {
    ProductVariant checkAndProductVariantAvailableWithQuantity(Long productVariantId, int quantity);

    void checkProductVariantsSameShop(List<Long> productVariantIds, Long shopId);

    List<ProductVariant> filterProductVariantsByStatus(List<ProductVariant> productVariants, Status status);

    void checkDuplicateProductVariantIds(List<Long> productVariantIds);

    ProductVariant getProductVariantById(Long productVariantId);

    @Transactional
    void updateProductVariantQuantity(Long productVariantId, int quantity);
}
