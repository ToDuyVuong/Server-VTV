package hcmute.kltn.vtv.service.guest.impl;

import hcmute.kltn.vtv.model.entity.vendor.ProductVariant;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.vendor.ProductVariantRepository;
import hcmute.kltn.vtv.service.guest.IProductVariantService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductVariantServiceImpl implements IProductVariantService {


    @Autowired
    private ProductVariantRepository productVariantRepository;


    @Override
    public ProductVariant checkAndProductVariantAvailableWithQuantity(Long productVariantId, int quantity) {
        ProductVariant productVariant = productVariantRepository
                .findById(productVariantId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy biến thể sản phẩm có mã: " + productVariantId));

        if (!productVariant.getStatus().equals(Status.ACTIVE)) {
            throw new BadRequestException("Sản phẩm không khả dụng.");
        }

        if (productVariant.getQuantity() <= 0) {
            throw new BadRequestException("Sản phẩm đã hết hàng.");
        }

        if (productVariant.getQuantity() < quantity) {
            throw new BadRequestException("Số lượng sản phẩm trong kho không đủ.");
        }

        return productVariant;
    }


    @Override
    public void checkProductVariantsSameShop(List<Long> productVariantIds, Long shopId) {
        List<ProductVariant> productVariants = productVariantRepository.findByProductVariantIdIn(productVariantIds)
                .orElseThrow(() -> new NotFoundException("Không tìm biến thể sản phẩm trong danh sách mã biến thể sản phẩm!"));

        if (productVariants.stream().anyMatch(productVariant -> !productVariant.getProduct().getShop().getShopId().equals(shopId))) {
            throw new BadRequestException("Sản phẩm không thuộc cùng một cửa hàng!");
        }

        if (productVariants.stream().anyMatch(productVariant -> !productVariant.getStatus().equals(Status.ACTIVE))) {
            throw new BadRequestException("Có biến thể sản phẩm không khả dụng trong danh sách biến thể sản phẩm!");
        }
    }


    @Override
    public List<ProductVariant> filterProductVariantsByStatus(List<ProductVariant> productVariants, Status status) {
        return productVariants.stream()
                .filter(productVariant -> productVariant.getStatus() == status)
                .toList();
    }



    @Override
    public void checkDuplicateProductVariantIds(List<Long> productVariantIds) {
        if (productVariantIds.size() != productVariantIds.stream().distinct().count()) {
            throw new BadRequestException("Danh sách sản phẩm không được trùng lặp!");
        }
    }


    @Override
    public ProductVariant getProductVariantById(Long productVariantId) {
        return productVariantRepository.findById(productVariantId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy biến thể sản phẩm!"));
    }
}
