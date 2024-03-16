package hcmute.kltn.vtv.model.dto.vendor;

import hcmute.kltn.vtv.model.entity.user.Review;
import hcmute.kltn.vtv.model.entity.vendor.Product;
import hcmute.kltn.vtv.model.extra.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@Data
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class ProductDTO {

    private Long productId;

    private String name;

    private String image;

    private String description;

    private String information;

    private Long sold;

    private Status status;

    private Long categoryId;

    private Long shopId;

    private Long brandId;

    private Long maxPrice;

    private Long minPrice;

    private String rating;

    private int countProductVariant;

    private List<ProductVariantDTO> productVariantDTOs;


    public static List<ProductDTO> convertToListDTO(List<Product> products) {
        List<ProductDTO> productDTOs = new ArrayList<>();
        for (Product product : products) {
            productDTOs.add(convertEntityToDTO(product));
        }
        return productDTOs;
    }

    public static ProductDTO convertEntityToDTO(Product product) {

        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(product.getProductId());
        productDTO.setName(product.getName());
        productDTO.setImage(product.getImage());
        productDTO.setDescription(product.getDescription());
        productDTO.setInformation(product.getInformation());
        productDTO.setSold(product.getSold());
        productDTO.setStatus(product.getStatus());
        productDTO.setCategoryId(product.getCategory().getCategoryId());
        productDTO.setShopId(product.getShop().getShopId());
        productDTO.setBrandId(product.getBrand() != null ? product.getBrand().getBrandId() : null);
        productDTO.setCountProductVariant(product.getProductVariants().size());
        productDTO.setProductVariantDTOs(ProductVariantDTO.convertToListDTO(product.getProductVariants()));

        productDTO.setMinPrice(calculateMinPrices(productDTO.getProductVariantDTOs()));
        productDTO.setMaxPrice(calculateMaxPrices(productDTO.getProductVariantDTOs()));
        productDTO.setRating(calculateAverageRating(product.getReviews()));

        return productDTO;
    }

    private static Long calculateMinPrices(List<ProductVariantDTO> productVariantDTOs) {
        Long minPrice = Long.MAX_VALUE;
        for (ProductVariantDTO productVariantDTO : productVariantDTOs) {
            if (productVariantDTO.getPrice() < minPrice) {
                minPrice = productVariantDTO.getPrice();
            }
        }
        return minPrice;
    }

    private static Long calculateMaxPrices(List<ProductVariantDTO> productVariantDTOs) {
        Long maxPrice = Long.MIN_VALUE;
        for (ProductVariantDTO productVariantDTO : productVariantDTOs) {
            if (productVariantDTO.getPrice() > maxPrice) {
                maxPrice = productVariantDTO.getPrice();
            }
        }
        return maxPrice;
    }


    private static String calculateAverageRating(List<Review> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return "0.0";
        }

        OptionalDouble averageRating = reviews.stream()
                .mapToDouble(Review::getRating)
                .average();

        // Format the String to have one decimal place
        return String.format("%.1f", averageRating.orElse(0.0));
    }



}
