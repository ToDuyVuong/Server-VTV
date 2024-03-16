package hcmute.kltn.vtv.model.data.vendor.response;

import hcmute.kltn.vtv.model.dto.vendor.AttributeDTO;
import hcmute.kltn.vtv.model.entity.vendor.Attribute;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AttributeResponse extends ResponseAbstract {

    private AttributeDTO attributeDTO;

    public static AttributeResponse attributeResponse(Attribute attribute, String message, String status) {
        AttributeResponse response = new AttributeResponse();
        response.setStatus(status);
        response.setMessage(message);
        response.setCode(200);
        response.setAttributeDTO(AttributeDTO.convertEntityToDTO(attribute));
        return response;
    }
}
