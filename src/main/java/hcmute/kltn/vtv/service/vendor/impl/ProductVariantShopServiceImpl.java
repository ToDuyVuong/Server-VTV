package hcmute.kltn.vtv.service.vendor.impl;

import hcmute.kltn.vtv.repository.vendor.ProductVariantRepository;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.vendor.request.ProductVariantRequest;
import hcmute.kltn.vtv.model.entity.vendor.Attribute;
import hcmute.kltn.vtv.model.entity.vendor.ProductVariant;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.service.vendor.IAttributeShopService;
import hcmute.kltn.vtv.service.vendor.IProductVariantShopService;
import lombok.RequiredArgsConstructor;
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
            productVariants.add(addNewProductVariant(request, shopId));
        }
        productVariants.sort(Comparator.comparing(ProductVariant::getSku));

        return productVariants;
    }

    @Override
    public List<ProductVariant> getListProductVariant(List<ProductVariantRequest> requests,
                                                      Long shopId, Long productId) {

        List<ProductVariant> productVariants = editListProductVariantByProductVariantRequest(requests, shopId, productId);
        productVariants.sort(Comparator.comparing(ProductVariant::getSku));

        return productVariants;
    }


    private List<ProductVariant> editListProductVariantByProductVariantRequest(List<ProductVariantRequest> requests,
                                                                               Long shopId, Long productId) {

        List<ProductVariant> productVariants = addNewProductVariantNotExisted(requests, shopId);

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

        return productVariants;
    }


    private List<ProductVariant> addNewProductVariantNotExisted(List<ProductVariantRequest> requests, Long shopId) {
        List<ProductVariant> productVariants = new ArrayList<>();
        for (ProductVariantRequest request : requests) {
            ProductVariant productVariant = updateProductVariant(request, shopId);
            if (productVariant == null) {
                productVariant = addNewProductVariant(request, shopId);
            }
            productVariants.add(productVariant);
        }

        return productVariants;
    }



    private ProductVariant updateProductVariant(ProductVariantRequest request, Long shopId) {
        ProductVariant productVariant = productVariantRepository.findByProductVariantId(request.getProductVariantId());
        if (productVariant == null) {
            return null;
        }

        List<Attribute> attributes = getListAttributeByAttributeIds(request.getAttributeIds(), shopId);
        checkSameSkuProductVariant(request.getSku(), productVariant);

        return editProductVariantByProductVariantRequest(productVariant, request, attributes);
    }


    private void checkSameSkuProductVariant(String sku, ProductVariant productVariant) {
        if (!sku.equals(productVariant.getSku()) && productVariantRepository
                .existsBySkuAndProductProductId(sku,
                        productVariant.getProduct().getProductId())) {
            throw new BadRequestException("Mã biến thể sản phẩm đã tồn tại trong sản phẩm!");
        }
    }


    private ProductVariant addNewProductVariant(ProductVariantRequest request, Long shopId) {
        List<Attribute> attributes = getListAttributeByAttributeIds(request.getAttributeIds(), shopId);

        try {
            return createProductVariantByProductVariantRequest(request, attributes);
        } catch (Exception e) {
            throw new BadRequestException("Thêm biến thể sản phẩm thất bại!");
        }
    }


    private List<Attribute> getListAttributeByAttributeIds(List<Long> attributeIds, Long shopId) {
        List<Attribute> attributes = new ArrayList<>();
        if (attributeIds != null && !attributeIds.isEmpty()) {
            attributes = attributeService.getListAttributeByListId(attributeIds, shopId);
        }
        return attributes;
    }


    private ProductVariant createProductVariantByProductVariantRequest(ProductVariantRequest request, List<Attribute> attributes) {

        ProductVariant productVariant = new ProductVariant();
        productVariant.setSku(request.getSku());
        productVariant.setImage(request.getImage());
        productVariant.setOriginalPrice(request.getOriginalPrice());
        productVariant.setPrice(request.getPrice());
        productVariant.setQuantity(request.getQuantity());
        productVariant.setStatus(Status.ACTIVE);
        productVariant.setCreateAt(LocalDateTime.now());
        productVariant.setAttributes(attributes);

        return productVariant;
    }


    private ProductVariant editProductVariantByProductVariantRequest(ProductVariant productVariant, ProductVariantRequest request, List<Attribute> attributes) {

        productVariant.setSku(request.getSku());
        productVariant.setImage(request.getImage());
        productVariant.setPrice(request.getPrice());
        productVariant.setQuantity(request.getQuantity());
        productVariant.setAttributes(attributes);
        productVariant.setCreateAt(LocalDateTime.now());

        return productVariant;
    }


}
