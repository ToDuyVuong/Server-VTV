package hcmute.kltn.vtv.model.data.shipping.response;

import hcmute.kltn.vtv.model.dto.shipping.DeliverDTO;
import hcmute.kltn.vtv.model.entity.shipping.Deliver;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DeliverResponse extends ResponseAbstract {

    private DeliverDTO deliverDTO;



    public static DeliverResponse deliverResponse(Deliver deliver, String message, String status) {
        DeliverResponse response = new DeliverResponse();
        response.setDeliverDTO(DeliverDTO.convertEntityToDTO(deliver));
        response.setCode(200);
        response.setMessage(message);
        response.setStatus(status);

        return response;
    }
}
