package hcmute.kltn.vtv.repository.vendor;

import hcmute.kltn.vtv.model.entity.vendor.ProductVariant;
import hcmute.kltn.vtv.model.extra.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {

    ProductVariant findByProductVariantId(Long productVariantId);

    boolean existsBySkuAndProductProductId(String sku, Long productId);

    boolean existsByProductProductIdAndProductVariantId( Long productId, Long productVariantId);

    boolean existsBySkuAndProductProductIdAndProductVariantIdNot(String sku, Long productId, Long productVariantId);

    List<ProductVariant> findAllByProductProductId(Long productId);

    Optional<List<ProductVariant>> findAllByProductProductIdAndStatus(Long productId, Status status);

    ProductVariant getProductByProductVariantId(Long productVariantId);


    Optional<List<ProductVariant>> findByProductVariantIdIn(List<Long> productVariantIds);

}
