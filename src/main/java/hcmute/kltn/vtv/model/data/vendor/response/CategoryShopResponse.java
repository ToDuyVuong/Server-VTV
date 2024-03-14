package hcmute.kltn.vtv.model.data.vendor.response;

import hcmute.kltn.vtv.model.dto.vendor.CategoryShopDTO;
import hcmute.kltn.vtv.model.entity.vendor.CategoryShop;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CategoryShopResponse extends ResponseAbstract {

    private CategoryShopDTO categoryShopDTO;

    public static CategoryShopResponse categoryShopResponse(CategoryShop categoryShop, String message, String status) {
        CategoryShopResponse categoryShopResponse = new CategoryShopResponse();
        categoryShopResponse.setCategoryShopDTO(CategoryShopDTO.convertEntityToDTO(categoryShop));
        categoryShopResponse.setMessage(message);
        categoryShopResponse.setStatus(status);
        categoryShopResponse.setCode(200);

        return categoryShopResponse;
    }
}
