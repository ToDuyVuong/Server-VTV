package hcmute.kltn.vtv.model.dto.user;

import hcmute.kltn.vtv.model.dto.vendor.ProductDTO;
import hcmute.kltn.vtv.model.dto.vendor.ProductVariantDTO;
import hcmute.kltn.vtv.model.entity.user.FavoriteProduct;
import hcmute.kltn.vtv.model.entity.vendor.Product;
import hcmute.kltn.vtv.model.entity.vendor.ProductVariant;
import hcmute.kltn.vtv.model.extra.Status;
import lombok.*;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteProductDTO {

    private Long favoriteProductId;

    private Long productId;

    private LocalDateTime createAt;

    public static FavoriteProductDTO convertEntityToDTO(FavoriteProduct favoriteProduct) {
        FavoriteProductDTO favoriteProductDTO = new FavoriteProductDTO();
        favoriteProductDTO.setFavoriteProductId(favoriteProduct.getFavoriteProductId());
        favoriteProductDTO.setProductId(favoriteProduct.getProduct().getProductId());
        favoriteProductDTO.setCreateAt(favoriteProduct.getCreateAt());
        return favoriteProductDTO;
    }

    public static List<FavoriteProductDTO> convertToEntitiesDTOs(List<FavoriteProduct> favoriteProducts) {
        favoriteProducts.sort((o1, o2) -> o2.getCreateAt().compareTo(o1.getCreateAt()));

        List<FavoriteProductDTO> favoriteProductDTOs = new ArrayList<>();
        for (FavoriteProduct favoriteProduct : favoriteProducts) {
            FavoriteProductDTO favoriteProductDTO = convertEntityToDTO(favoriteProduct);
            favoriteProductDTOs.add(favoriteProductDTO);
        }
        return favoriteProductDTOs;

    }

    private static ProductDTO getProductToDTO(Product product) {
        ModelMapper modelMapper = new ModelMapper();
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
        List<ProductVariant> activeProductVariants = product.getProductVariants().stream()
                .filter(productVariant -> productVariant.getStatus() != Status.DELETED)
                .toList();

        productDTO.setProductVariantDTOs(ProductVariantDTO.convertToListDTO(activeProductVariants));
        return productDTO;
    }

}
