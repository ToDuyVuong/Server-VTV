package hcmute.kltn.vtv.model.data.guest;

import hcmute.kltn.vtv.model.dto.vtv.CategoryDTO;
import hcmute.kltn.vtv.model.entity.vtv.Category;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse extends ResponseAbstract {

    private CategoryDTO categoryDTO;


    public static  CategoryResponse categoryResponse(Category category, String message, String status) {
        CategoryResponse response = new CategoryResponse();
        response.setCategoryDTO(CategoryDTO.convertEntityToDTO(category));
        response.setCode(200);
        response.setMessage(message);
        response.setStatus(status);

        return response;
    }

}
