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
public class CategoryResponse extends ResponseAbstract {

    private List<CategoryDTO> categoryDTOs;


    public static CategoryResponse categoryResponse(List<Category> categories, String message, String status) {

        CategoryResponse response = new CategoryResponse();
        response.setCategoryDTOs(CategoryDTO.convertToListDTO(categories));
        response.setCode(200);
        response.setMessage(message);
        response.setStatus(status);

        return response;
    }
}
