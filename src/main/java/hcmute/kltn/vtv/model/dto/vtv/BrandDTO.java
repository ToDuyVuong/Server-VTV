package hcmute.kltn.vtv.model.dto.vtv;

import hcmute.kltn.vtv.model.entity.vtv.Brand;
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

    private Status status;

    public void validate() {
        if (name == null || name.isEmpty()) {
            throw new BadRequestException("Tên thương hiệu không được để trống.");
        }
        if (description == null || description.isEmpty()) {
            throw new BadRequestException("Mô tả không được để trống.");
        }
        if (information == null || information.isEmpty()) {
            throw new BadRequestException("Thông tin không được để trống.");
        }
        if (origin == null || origin.isEmpty()) {
            throw new BadRequestException("Xuất xứ không được để trống.");
        }
        if (image == null || image.isEmpty()) {
            throw new BadRequestException("Hình ảnh không được để trống.");
        }
        if (status == null) {
            throw new BadRequestException("Trạng thái không được để trống.");
        }
    }


    public static BrandDTO convertEntityToDTO(Brand brand) {
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setBrandId(brand.getBrandId());
        brandDTO.setName(brand.getName());
        brandDTO.setImage(brand.getImage());
        brandDTO.setDescription(brand.getDescription());
        brandDTO.setInformation(brand.getInformation());
        brandDTO.setOrigin(brand.getOrigin());
        brandDTO.setStatus(brand.getStatus());

        return brandDTO;
    }

    public static List<BrandDTO> convertEntitiesToDTOs(List<Brand> brands) {
        List<BrandDTO> brandDTOS = new ArrayList<>();
        for (Brand brand : brands) {
            brandDTOS.add(convertEntityToDTO(brand));
        }
        return brandDTOS;
    }
}
