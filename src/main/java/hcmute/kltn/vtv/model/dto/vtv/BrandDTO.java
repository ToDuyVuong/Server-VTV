package hcmute.kltn.vtv.model.dto.vtv;

import hcmute.kltn.vtv.model.entity.vtv.Brand;
import hcmute.kltn.vtv.model.entity.vtv.Category;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.*;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BrandDTO {

    private Long brandId;

    private String name;

    private String image;

    private String description;

    private String information;

    private String origin;

    private boolean allCategories;

    private List<Long> categories;


    public static BrandDTO convertEntityToDTO(Brand brand) {
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setBrandId(brand.getBrandId());
        brandDTO.setName(brand.getName());
        brandDTO.setImage(brand.getImage());
        brandDTO.setDescription(brand.getDescription());
        brandDTO.setInformation(brand.getInformation());
        brandDTO.setOrigin(brand.getOrigin());
        brandDTO.setAllCategories(brand.isAllCategories());
        brandDTO.setCategories(brandDTO.isAllCategories() ? new ArrayList<>() :
                brand.getCategories().stream().map(Category::getCategoryId).toList());


        return brandDTO;
    }

    public static List<BrandDTO> convertEntitiesToDTOs(List<Brand> brands) {
        List<BrandDTO> brandDTOS = new ArrayList<>();
        for (Brand brand : brands) {
            brandDTOS.add(convertEntityToDTO(brand));
        }
        brandDTOS.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
        return brandDTOS;
    }
}
