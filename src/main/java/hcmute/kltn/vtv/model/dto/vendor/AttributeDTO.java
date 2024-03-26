package hcmute.kltn.vtv.model.dto.vendor;

import hcmute.kltn.vtv.model.entity.vendor.Attribute;
import lombok.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AttributeDTO {

    private Long attributeId;

    private String name;

    private String value;

    private boolean active;

    public static List<AttributeDTO> convertEntitiesToDTOs(List<Attribute> attributes) {
        List<AttributeDTO> attributeDTOS = new ArrayList<>();
        for (Attribute attribute : attributes) {
            AttributeDTO attributeDTO = convertEntityToDTO(attribute);
            attributeDTOS.add(attributeDTO);
        }
        attributeDTOS.sort(Comparator.comparing(AttributeDTO::getName).thenComparing(AttributeDTO::getValue));

        return attributeDTOS;
    }

    public static AttributeDTO convertEntityToDTO(Attribute attribute) {
        AttributeDTO attributeDTO = new AttributeDTO();
        attributeDTO.setAttributeId(attribute.getAttributeId());
        attributeDTO.setName(attribute.getName());
        attributeDTO.setValue(attribute.getValue());
        attributeDTO.setActive(attribute.isActive());

        return attributeDTO;
    }
}
