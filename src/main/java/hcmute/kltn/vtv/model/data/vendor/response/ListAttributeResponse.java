package hcmute.kltn.vtv.model.data.vendor.response;

import hcmute.kltn.vtv.model.dto.vendor.AttributeDTO;
import hcmute.kltn.vtv.model.entity.vendor.Attribute;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ListAttributeResponse extends ResponseAbstract {

    private int count;
    private List<AttributeDTO> attributeDTOs;

    public static ListAttributeResponse listAttributeResponse(List<Attribute> attributes, String message, String status) {
        ListAttributeResponse listAttributeResponse = new ListAttributeResponse();
        listAttributeResponse.setStatus(status);
        listAttributeResponse.setMessage(message);
        listAttributeResponse.setCode(200);
        listAttributeResponse.setCount(attributes.size());
        listAttributeResponse.setAttributeDTOs(AttributeDTO.convertToListDTO(attributes));
        return listAttributeResponse;
    }
}