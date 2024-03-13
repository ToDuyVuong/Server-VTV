package hcmute.kltn.vtv.model.data.vendor.response;

import hcmute.kltn.vtv.model.dto.vendor.CategoryShopDTO;
import hcmute.kltn.vtv.model.dto.vtv.CategoryDTO;
import hcmute.kltn.vtv.model.entity.vendor.CategoryShop;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ListCategoryShopResponse extends ResponseAbstract {

    private int count;
    private List<CategoryShopDTO> categoryShopDTOs;


    public static ListCategoryShopResponse listCategoryShopResponse(List<CategoryShop> categoryShops, String message, String status) {
        ListCategoryShopResponse listCategoryShopResponse = new ListCategoryShopResponse();
        listCategoryShopResponse.setCount(categoryShops.size());
        listCategoryShopResponse.setCategoryShopDTOs(CategoryShopDTO.convertEntitiesToDTOs(categoryShops));
        listCategoryShopResponse.setMessage(message);
        listCategoryShopResponse.setStatus(status);
        listCategoryShopResponse.setCode(200);

        return listCategoryShopResponse;
    }
}
