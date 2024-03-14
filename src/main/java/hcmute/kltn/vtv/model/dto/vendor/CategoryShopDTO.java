package hcmute.kltn.vtv.model.dto.vendor;

import hcmute.kltn.vtv.model.entity.vendor.CategoryShop;
import hcmute.kltn.vtv.model.entity.vendor.Product;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CategoryShopDTO {

    private Long categoryShopId;

    private String name;

    private String image;

    private int countProduct;

    private List<Long> productIds;


    public static CategoryShopDTO convertEntityToDTO(CategoryShop categoryShop) {
        CategoryShopDTO categoryShopDTO = new CategoryShopDTO();
        categoryShopDTO.setCategoryShopId(categoryShop.getCategoryShopId());
        categoryShopDTO.setName(categoryShop.getName());
        categoryShopDTO.setImage(categoryShop.getImage());
        categoryShopDTO.setProductIds(categoryShop.getProducts() == null ? new ArrayList<>() :
                categoryShop.getProducts().stream().map(Product::getProductId).toList());
        categoryShopDTO.setCountProduct(categoryShop.getProducts().size());
        return categoryShopDTO;
    }

    public static List<CategoryShopDTO> convertEntitiesToDTOs(List<CategoryShop> categoryShops) {
        List<CategoryShopDTO> categoryShopDTOs = new ArrayList<>();
        for (CategoryShop categoryShop : categoryShops) {
            categoryShopDTOs.add(convertEntityToDTO(categoryShop));
        }
        categoryShopDTOs.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
        return categoryShopDTOs;
    }

}
