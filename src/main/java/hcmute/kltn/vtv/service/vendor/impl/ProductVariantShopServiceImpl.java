package hcmute.kltn.vtv.service.vendor.impl;

import hcmute.kltn.vtv.model.entity.vendor.Product;
import hcmute.kltn.vtv.repository.vendor.ProductVariantRepository;
import hcmute.kltn.vtv.service.vtv.IImageService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.vendor.request.ProductVariantRequest;
import hcmute.kltn.vtv.model.entity.vendor.Attribute;
import hcmute.kltn.vtv.model.entity.vendor.ProductVariant;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.service.vendor.IAttributeShopService;
import hcmute.kltn.vtv.service.vendor.IProductVariantShopService;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductVariantShopServiceImpl implements IProductVariantShopService {

    @Autowired
    private final IAttributeShopService attributeService;
    @Autowired
    private final ProductVariantRepository productVariantRepository;
    @Autowired
    private final IImageService imageService;


    @Override
    @Transactional
    public List<ProductVariant> addNewProductVariants(List<ProductVariantRequest> requests, Product product) {
        try {
            List<ProductVariant> productVariants = new ArrayList<>();
            for (ProductVariantRequest request : requests) {
                productVariants.add(addNewProductVariant(request, product));
            }

            return productVariants;
        } catch (Exception e) {
            throw new BadRequestException("Thêm danh sách biến thể sản phẩm thất bại!");
        }
    }


    @Override
    @Transactional
    public ProductVariant addNewProductVariant(ProductVariantRequest request, Product product) {
        checkExistProductVariantBySku(request.getSku(), product.getProductId());
        List<Attribute> attributes = new ArrayList<>();
        if (request.getProductAttributeRequests() != null && !request.getProductAttributeRequests().isEmpty()) {
            attributes = attributeService.addNewAttributesByProductAttributeRequests(request.getProductAttributeRequests(), product.getShop());
        }
        ProductVariant productVariant = createProductVariantByProductVariantRequest(request, attributes, product);
        try {

            return productVariantRepository.save(productVariant);
        } catch (Exception e) {
            imageService.deleteImageInFirebase(productVariant.getImage());

            throw new BadRequestException("Thêm biến thể sản phẩm thất bại!");
        }
    }


    @Override
    @Transactional
    public List<ProductVariant> updateProductVariants(List<ProductVariantRequest> requests, Product product) {
        try {
            List<ProductVariant> productVariantsOld = productVariantRepository
                    .findAllByProductProductIdAndStatus(product.getProductId(), Status.ACTIVE)
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy biến thể sản phẩm!"));
            List<ProductVariant> productVariantsCurrent = new ArrayList<>();
            for (ProductVariantRequest request : requests) {
                productVariantsCurrent.add(updateProductVariant(request, product));
            }
            List<ProductVariant> missingProductVariants = findMissingProductVariants(productVariantsOld, productVariantsCurrent);
            if (!missingProductVariants.isEmpty()) {
                updateStatusProductVariants(product.getProductId(), missingProductVariants, Status.DELETED);
            }


            return productVariantsCurrent;
        } catch (Exception e) {
            throw new BadRequestException("Cập nhật danh sách biến thể sản phẩm thất bại!");
        }
    }


    @Override
    @Transactional
    public ProductVariant updateProductVariant(ProductVariantRequest request, Product product) {
        if (request.getProductVariantId() == null) {
            return addNewProductVariant(request, product);
        } else {
            checkExistProductVariantByIdAndProductId(product.getProductId(), request.getProductVariantId());
            ProductVariant productVariant = productVariantRepository.findByProductVariantId(request.getProductVariantId());
            List<Attribute> attributes = new ArrayList<>();
            if (request.getProductAttributeRequests() != null && !request.getProductAttributeRequests().isEmpty()) {
                attributes = attributeService.addNewAttributesByProductAttributeRequests(request.getProductAttributeRequests(), product.getShop());
            }
            checkSameSkuProductVariant(request.getSku(), productVariant);
            updateProductVariantByProductVariantRequest(productVariant, request, attributes);
            try {

                return productVariantRepository.save(productVariant);
            } catch (Exception e) {
                throw new BadRequestException("Cập nhật biến thể sản phẩm thất bại!");
            }
        }
    }


    @Override
    @Transactional
    public List<ProductVariant> updateStatusProductVariants(Long productId, List<ProductVariant> productVariants, Status status) {
        for (ProductVariant productVariant : productVariants) {
            updateStatusProductVariant(productId, productVariant, status);
        }

        return productVariants;
    }


    @Transactional
    public ProductVariant updateStatusProductVariant (Long productId, ProductVariant productVariant, Status status) {
        checkExistProductVariantByIdAndProductId(productId, productVariant.getProductVariantId());
        productVariant.setStatus(status);
        productVariant.setUpdateAt(LocalDateTime.now());
        try {

            return productVariantRepository.save(productVariant);
        } catch (Exception e) {
            throw new BadRequestException("Cập nhật biến thể sản phẩm thất bại!");
        }
    }


    public List<ProductVariant> findMissingProductVariants(List<ProductVariant> productVariantsOld, List<ProductVariant> productVariantsCurrent) {
        List<ProductVariant> missingProductVariants = new ArrayList<>();
        Set<Long> currentProductVariantIds = productVariantsCurrent.stream()
                .map(ProductVariant::getProductVariantId)
                .collect(Collectors.toSet());

        for (ProductVariant productVariantOld : productVariantsOld) {
            if (!currentProductVariantIds.contains(productVariantOld.getProductVariantId())) {
                missingProductVariants.add(productVariantOld);
            }
        }
        return missingProductVariants;
    }



    private void checkExistProductVariantByIdAndProductId(Long productId, Long productVariantId) {
        if (!productVariantRepository.existsByProductProductIdAndProductVariantId(productId, productVariantId)) {
            throw new BadRequestException("Biến thể sản phẩm không tồn tại trong sản phẩm!");
        }
    }

    private ProductVariant createProductVariantByProductVariantRequest(ProductVariantRequest request, List<Attribute> attributes, Product product) {
        ProductVariant productVariant = new ProductVariant();
        productVariant.setSku(request.getSku());
        productVariant.setImage(imageService.uploadImageToFirebase(request.getImage()));
        productVariant.setOriginalPrice(request.getOriginalPrice());
        productVariant.setPrice(request.getPrice());
        productVariant.setQuantity(request.getQuantity());
        productVariant.setStatus(Status.ACTIVE);
        productVariant.setCreateAt(LocalDateTime.now());
        productVariant.setUpdateAt(LocalDateTime.now());
        productVariant.setAttributes(attributes);
        productVariant.setProduct(product);

        return productVariant;
    }


    private void updateProductVariantByProductVariantRequest(ProductVariant productVariant, ProductVariantRequest request, List<Attribute> attributes) {
        productVariant.setSku(request.getSku());
        productVariant.setImage(request.isChangeImage() ? imageService.uploadImageToFirebase(request.getImage()) : productVariant.getImage());
        productVariant.setOriginalPrice(request.getOriginalPrice());
        productVariant.setPrice(request.getPrice());
        productVariant.setQuantity(request.getQuantity());
        productVariant.setAttributes(attributes);
        productVariant.setUpdateAt(LocalDateTime.now());
    }


    private void checkExistProductVariantBySku(String sku, Long productId) {
        if (productVariantRepository.existsBySkuAndProductProductId(sku, productId)) {
            throw new BadRequestException("Mã biến thể sản phẩm đã tồn tại trong sản phẩm!");
        }
    }


    private void checkSameSkuProductVariant(String sku, ProductVariant productVariant) {


        if (productVariantRepository.existsBySkuAndProductProductIdAndProductVariantIdNot(sku, productVariant.getProduct().getProductId(), productVariant.getProductVariantId())) {
            throw new BadRequestException("Mã biến thể sản phẩm đã tồn tại trong sản phẩm!");
        }
    }


//    @Override
//    public List<ProductVariant> addNewListProductVariant(List<ProductVariantRequest> requests, Long shopId) {
//        List<ProductVariant> productVariants = new ArrayList<>();
//        for (ProductVariantRequest request : requests) {
//            productVariants.add(addNewProductVariant(request, shopId));
//        }
//        productVariants.sort(Comparator.comparing(ProductVariant::getSku));
//
//        return productVariants;
//    }
//
//    @Override
//    public List<ProductVariant> getListProductVariant(List<ProductVariantRequest> requests,
//                                                      Long shopId, Long productId) {
//
//        List<ProductVariant> productVariants = editListProductVariantByProductVariantRequest(requests, shopId, productId);
//        productVariants.sort(Comparator.comparing(ProductVariant::getSku));
//
//        return productVariants;
//    }
//
//
//    private List<ProductVariant> editListProductVariantByProductVariantRequest(List<ProductVariantRequest> requests,
//                                                                               Long shopId, Long productId) {
//
//        List<ProductVariant> productVariants = addNewProductVariantNotExisted(requests, shopId);
//
//        List<ProductVariant> existingVariants = productVariantRepository.findAllByProductProductId(productId);
//
//        for (ProductVariant existingVariant : existingVariants) {
//            boolean foundInRequests = false;
//            for (ProductVariantRequest request : requests) {
//                if (existingVariant.getProductVariantId().equals(request.getProductVariantId())) {
//                    foundInRequests = true;
//                    break;
//                }
//            }
//
//            if (!foundInRequests) {
//                existingVariant.setStatus(Status.DELETED);
//                existingVariant.setUpdateAt(LocalDateTime.now());
//                productVariants.add(existingVariant);
//            }
//        }
//
//        return productVariants;
//    }
//
//
//    private List<ProductVariant> addNewProductVariantNotExisted(List<ProductVariantRequest> requests, Long shopId) {
//        List<ProductVariant> productVariants = new ArrayList<>();
//        for (ProductVariantRequest request : requests) {
//            ProductVariant productVariant = updateProductVariant(request, shopId);
//            if (productVariant == null) {
//                productVariant = addNewProductVariant(request, shopId);
//            }
//            productVariants.add(productVariant);
//        }
//
//        return productVariants;
//    }
//
//
//
//    private ProductVariant updateProductVariant(ProductVariantRequest request, Long shopId) {
//        ProductVariant productVariant = productVariantRepository.findByProductVariantId(request.getProductVariantId());
//        if (productVariant == null) {
//            return null;
//        }
//
//        List<Attribute> attributes = getListAttributeByAttributeIds(request.getAttributeIds(), shopId);
//        checkSameSkuProductVariant(request.getSku(), productVariant);
//
//        return editProductVariantByProductVariantRequest(productVariant, request, attributes);
//    }
//
//
//    private void checkSameSkuProductVariant(String sku, ProductVariant productVariant) {
//        if (!sku.equals(productVariant.getSku()) && productVariantRepository
//                .existsBySkuAndProductProductId(sku,
//                        productVariant.getProduct().getProductId())) {
//            throw new BadRequestException("Mã biến thể sản phẩm đã tồn tại trong sản phẩm!");
//        }
//    }
//
//
//    private ProductVariant addNewProductVariant(ProductVariantRequest request, Long shopId) {
//        List<Attribute> attributes = getListAttributeByAttributeIds(request.getAttributeIds(), shopId);
//
//        try {
//            return createProductVariantByProductVariantRequest(request, attributes);
//        } catch (Exception e) {
//            throw new BadRequestException("Thêm biến thể sản phẩm thất bại!");
//        }
//    }
//
//
//    private List<Attribute> getListAttributeByAttributeIds(List<Long> attributeIds, Long shopId) {
//        List<Attribute> attributes = new ArrayList<>();
//        if (attributeIds != null && !attributeIds.isEmpty()) {
//            attributes = attributeService.getListAttributeByListId(attributeIds, shopId);
//        }
//        return attributes;
//    }
//
//
//    private ProductVariant createProductVariantByProductVariantRequest(ProductVariantRequest request, List<Attribute> attributes) {
//
//        ProductVariant productVariant = new ProductVariant();
//        productVariant.setSku(request.getSku());
//        productVariant.setImage(request.getImage());
//        productVariant.setOriginalPrice(request.getOriginalPrice());
//        productVariant.setPrice(request.getPrice());
//        productVariant.setQuantity(request.getQuantity());
//        productVariant.setStatus(Status.ACTIVE);
//        productVariant.setCreateAt(LocalDateTime.now());
//        productVariant.setAttributes(attributes);
//
//        return productVariant;
//    }
//
//
//    private ProductVariant editProductVariantByProductVariantRequest(ProductVariant productVariant, ProductVariantRequest request, List<Attribute> attributes) {
//
//        productVariant.setSku(request.getSku());
//        productVariant.setImage(request.getImage());
//        productVariant.setPrice(request.getPrice());
//        productVariant.setQuantity(request.getQuantity());
//        productVariant.setAttributes(attributes);
//        productVariant.setCreateAt(LocalDateTime.now());
//
//        return productVariant;
//    }


}
