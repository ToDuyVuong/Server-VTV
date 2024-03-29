package hcmute.kltn.vtv.model.data.admin;

import hcmute.kltn.vtv.model.entity.vtv.Category;
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
public class CategoryAdminDTO {

    private Long categoryId;

    private String name;

    private String image;

    private String description;

    private boolean adminOnly;

    private Status status;

    public static List<CategoryAdminDTO> convertToListDTO(List<Category> categories) {

        List<CategoryAdminDTO> categoryAdminDTOS = new ArrayList<>();
        for (Category category : categories) {
            ModelMapper modelMapper = new ModelMapper();
            CategoryAdminDTO categoryAdminDTO = modelMapper.map(category, CategoryAdminDTO.class);
            categoryAdminDTOS.add(categoryAdminDTO);
        }
        return categoryAdminDTOS;

    }
}
