package hcmute.kltn.vtv.model.dto.vendor;

import hcmute.kltn.vtv.model.entity.vendor.ProductVariant;
import hcmute.kltn.vtv.model.extra.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class ProductVariantDTO {

    private Long productVariantId;

    private String sku;

    private String image;

    private Long originalPrice;

    private Long price;

    private int quantity;

    private Status status;

    private Long productId;

    private String productName;

    private String productImage;

    private String discountPercent;

    private List<AttributeDTO> attributeDTOs;

    public static ProductVariantDTO convertEntityToDTO(ProductVariant productVariant) {
        ProductVariantDTO productVariantDTO = new ProductVariantDTO();
        productVariantDTO.setProductVariantId(productVariant.getProductVariantId());
        productVariantDTO.setSku(productVariant.getSku());
        productVariantDTO.setImage(productVariant.getImage());
        productVariantDTO.setOriginalPrice(productVariant.getOriginalPrice());
        productVariantDTO.setPrice(productVariant.getPrice());
        productVariantDTO.setQuantity(productVariant.getQuantity());
        productVariantDTO.setStatus(productVariant.getStatus());
        productVariantDTO.setAttributeDTOs(AttributeDTO.convertToListDTO(productVariant.getAttributes()));
        productVariantDTO.setProductId(productVariant.getProduct().getProductId());
        productVariantDTO.setProductName(productVariant.getProduct().getName());
        productVariantDTO.setProductImage(productVariant.getProduct().getImage());
        productVariantDTO.setDiscountPercent(calculateDiscountPercentage(productVariant.getOriginalPrice(), productVariant.getPrice()));

        return productVariantDTO;
    }

    public static List<ProductVariantDTO> convertToListDTO(List<ProductVariant> productVariants) {
        List<ProductVariantDTO> productVariantDTOs = new ArrayList<>();

        for (ProductVariant productVariant : productVariants) {
            productVariantDTOs.add(convertEntityToDTO(productVariant));
        }
        return productVariantDTOs;
    }


    private static String calculateDiscountPercentage(Long originalPrice, Long currentPrice) {
        if (originalPrice == null || currentPrice == null || originalPrice <= 0) {
            return "0%";
        }

        double percentage = ((originalPrice - currentPrice) / (double) originalPrice) * 100;

        // Format the String based on the sign of the percentage
        if (percentage > 0) {
            return String.format("-%.0f%%", percentage);
        } else if (percentage < 0) {
            return String.format("%.0f%%", percentage);
        } else {
            return "0%";
        }
    }


}
