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
                .orElseThrow(() -> new NotFoundException("Không tìm thấy biến thể sản phẩm!"));

        if (productVariant.getStatus() == Status.DELETED ||
                productVariant.getProduct().getStatus() == Status.DELETED) {
            throw new BadRequestException("Sản phẩm đã bị xóa. " + productVariant.getProduct().getProductId());
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
    public List<ProductVariant> filterProductVariantsByStatus(List<ProductVariant> productVariants, Status status) {
        return productVariants.stream()
                .filter(productVariant -> productVariant.getStatus() == status)
                .toList();
    }
}
