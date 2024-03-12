package hcmute.kltn.vtv.model.dto.user;

import hcmute.kltn.vtv.model.dto.vendor.ProductDTO;
import hcmute.kltn.vtv.model.dto.vendor.ProductVariantDTO;
import hcmute.kltn.vtv.model.entity.user.FavoriteProduct;
import hcmute.kltn.vtv.model.entity.vendor.Product;
import hcmute.kltn.vtv.model.entity.vendor.ProductVariant;
import hcmute.kltn.vtv.model.extra.Status;
import lombok.*;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteProductDTO {

    private Long favoriteProductId;

    private ProductDTO productDTO;

    public static FavoriteProductDTO convertEntityToDTO(FavoriteProduct favoriteProduct) {
        ModelMapper modelMapper = new ModelMapper();
        FavoriteProductDTO favoriteProductDTO = new FavoriteProductDTO();
        favoriteProductDTO.setFavoriteProductId(favoriteProduct.getFavoriteProductId());
        favoriteProductDTO.setProductDTO(modelMapper.map(favoriteProduct.getProduct(), ProductDTO.class));
        return favoriteProductDTO;
    }

    public static List<FavoriteProductDTO> convertToEntitiesDTOs(List<FavoriteProduct> favoriteProducts) {
        favoriteProducts.sort((o1, o2) -> o2.getCreateAt().compareTo(o1.getCreateAt()));

        List<FavoriteProductDTO> favoriteProductDTOs = new ArrayList<>();
        for (FavoriteProduct favoriteProduct : favoriteProducts) {
            FavoriteProductDTO favoriteProductDTO = new FavoriteProductDTO();
            favoriteProductDTO.setFavoriteProductId(favoriteProduct.getFavoriteProductId());
            favoriteProductDTO.setProductDTO(getProductToDTO(favoriteProduct.getProduct()));
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
