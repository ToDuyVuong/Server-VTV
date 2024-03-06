package hcmute.kltn.vtv.service.vendor.impl;

import hcmute.kltn.vtv.repository.vtv.ProductVariantRepository;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.vendor.request.ProductVariantRequest;
import hcmute.kltn.vtv.model.entity.vtv.Attribute;
import hcmute.kltn.vtv.model.entity.vtv.ProductVariant;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.service.vendor.IAttributeShopService;
import hcmute.kltn.vtv.service.vendor.IProductVariantShopService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductVariantShopServiceImpl implements IProductVariantShopService {

    @Autowired
    private IAttributeShopService attributeService;
    @Autowired
    private ProductVariantRepository productVariantRepository;


    @Override
    public List<ProductVariant> addNewListProductVariant(List<ProductVariantRequest> requests, Long shopId) {

        List<ProductVariant> productVariants = new ArrayList<>();
        for (ProductVariantRequest request : requests) {
            ProductVariant productVariant = addNewProductVariant(request, shopId);
            productVariants.add(productVariant);
        }
        productVariants.sort(Comparator.comparing(ProductVariant::getSku));

        return productVariants;
    }

    @Override
    public List<ProductVariant> getListProductVariant(List<ProductVariantRequest> requests, Long shopId,
            Long productId) {

        List<ProductVariant> productVariants = new ArrayList<>();
        for (ProductVariantRequest request : requests) {
            ProductVariant productVariant = updateProductVariant(request, shopId);
            if (productVariant == null) {
                productVariant = addNewProductVariant(request, shopId);
            }

            productVariants.add(productVariant);
        }

        List<ProductVariant> existingVariants = productVariantRepository.findAllByProductProductId(productId);

        for (ProductVariant existingVariant : existingVariants) {
            boolean foundInRequests = false;
            for (ProductVariantRequest request : requests) {
                if (existingVariant.getProductVariantId().equals(request.getProductVariantId())) {
                    foundInRequests = true;
                    break;
                }
            }

            if (!foundInRequests) {
                existingVariant.setStatus(Status.DELETED);
                existingVariant.setUpdateAt(LocalDateTime.now());
                productVariants.add(existingVariant);
            }
        }

        productVariants.sort(Comparator.comparing(ProductVariant::getSku));

        return productVariants;
    }

    private ProductVariant updateProductVariant(ProductVariantRequest request, Long shopId) {
        ProductVariant productVariant = productVariantRepository.findByProductVariantId(request.getProductVariantId());
        if (productVariant == null) {
            return null;
        }

        List<Attribute> attributes = new ArrayList<>();
        if (request.getAttributeIds() != null && !request.getAttributeIds().isEmpty()) {
            attributes = attributeService.getListAttributeByListId(request.getAttributeIds(), shopId);
        }

        if (!request.getSku().equals(productVariant.getSku()) && productVariantRepository
                .existsBySkuAndProductProductId(request.getSku(),
                        productVariant.getProduct().getProductId())) {
            throw new BadRequestException("Mã biến thể sản phẩm đã tồn tại trong sản phẩm!");
        }

        productVariant.setSku(request.getSku());
        productVariant.setImage(request.getImage());
        productVariant.setPrice(request.getPrice());
        productVariant.setQuantity(request.getQuantity());
        productVariant.setAttributes(attributes);
        productVariant.setCreateAt(LocalDateTime.now());

        return productVariant;
    }

    private ProductVariant addNewProductVariant(ProductVariantRequest request, Long shopId) {
        List<Attribute> attributes = new ArrayList<>();
        if (request.getAttributeIds() != null && !request.getAttributeIds().isEmpty()) {
            attributes = attributeService.getListAttributeByListId(request.getAttributeIds(), shopId);
        }

        ProductVariant productVariant = modelMapper.map(request, ProductVariant.class);
        productVariant.setStatus(Status.ACTIVE);
        productVariant.setAttributes(attributes);

        return productVariant;
    }




}
