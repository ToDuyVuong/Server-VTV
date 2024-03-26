package hcmute.kltn.vtv.model.data.guest;


import hcmute.kltn.vtv.model.dto.vtv.CategoryDTO;
import hcmute.kltn.vtv.model.entity.vtv.Category;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CategoriesResponse extends ResponseAbstract {

    private List<CategoryDTO> categoryDTOs;


    public static CategoriesResponse categoriesResponse(List<Category> categories, String message, String status) {

        CategoriesResponse response = new CategoriesResponse();
        response.setCategoryDTOs(CategoryDTO.convertEntitiesToDTOs(categories));
        response.setCode(200);
        response.setMessage(message);
        response.setStatus(status);

        return response;
    }
}
