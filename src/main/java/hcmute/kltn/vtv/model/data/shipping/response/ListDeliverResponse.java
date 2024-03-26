package hcmute.kltn.vtv.model.data.shipping.response;

import hcmute.kltn.vtv.model.dto.shipping.DeliverDTO;
import hcmute.kltn.vtv.model.entity.shipping.Deliver;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ListDeliverResponse extends ResponseAbstract {

    private int count;
    private List<DeliverDTO> deliverDTOs;


    public static ListDeliverResponse listDeliverResponse(List<Deliver> delivers, String message, String status) {
        ListDeliverResponse response = new ListDeliverResponse();
        response.setCount(delivers.size());
        response.setDeliverDTOs(DeliverDTO.convertEntitiesToDTOs(delivers));
        response.setCode(200);
        response.setMessage(message);
        response.setStatus(status);

        return response;
    }
}
