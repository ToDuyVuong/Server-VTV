package hcmute.kltn.vtv.model.data.shipping.response;


import hcmute.kltn.vtv.model.dto.shipping.ShippingDTO;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ListShippingResponse extends ResponseAbstract {

    private int count;
    private List<ShippingDTO> shippingDTOs;

    public static ListShippingResponse listShippingResponse(List<ShippingDTO> shippingDTOs,
                                                            String message, String status) {
        ListShippingResponse listShippingResponse = new ListShippingResponse();
        listShippingResponse.setShippingDTOs(shippingDTOs);
        listShippingResponse.setCount(shippingDTOs.size());
        listShippingResponse.setMessage(message);
        listShippingResponse.setStatus(status);
        listShippingResponse.setCode(200);

        return listShippingResponse;
    }

}
