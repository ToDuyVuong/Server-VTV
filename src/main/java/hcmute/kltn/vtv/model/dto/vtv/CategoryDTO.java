package hcmute.kltn.vtv.model.dto.vtv;

import hcmute.kltn.vtv.model.entity.vtv.Category;
import hcmute.kltn.vtv.model.extra.Status;
import lombok.*;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    private Long categoryId;

    private String name;

    private String image;

    private String description;

    private boolean child;

    private Status status;

    private Long parentId;


    public static CategoryDTO convertEntityToDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryId(category.getCategoryId());
        categoryDTO.setName(category.getName());
        categoryDTO.setImage(category.getImage());
        categoryDTO.setDescription(category.getDescription());
        categoryDTO.setChild(category.isChild());
        categoryDTO.setStatus(category.getStatus());
        categoryDTO.setParentId(!category.isChild() ? null : category.getParent().getCategoryId());

        return categoryDTO;
    }

    public static List<CategoryDTO> convertToListDTO(List<Category> categories) {
        List<CategoryDTO> categoryDTOs = new ArrayList<>();
        categories.sort(Comparator.comparing(Category::getName));
        for (Category category : categories) {
            categoryDTOs.add(convertEntityToDTO(category));
        }
        return categoryDTOs;
    }

}
