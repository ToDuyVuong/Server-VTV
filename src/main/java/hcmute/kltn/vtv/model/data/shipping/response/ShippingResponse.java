package hcmute.kltn.vtv.model.data.shipping.response;


import hcmute.kltn.vtv.model.dto.shipping.ShippingDTO;
import hcmute.kltn.vtv.model.entity.shipping.TransportProvider;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShippingResponse extends ResponseAbstract {

    private ShippingDTO shippingDTO;




    public static ShippingResponse shippingResponse(ShippingDTO shippingDTO,
                                                    String message, String status) {
        ShippingResponse shippingResponse = new ShippingResponse();
        shippingResponse.setShippingDTO(shippingDTO);
        shippingResponse.setMessage(message);
        shippingResponse.setStatus(status);
        shippingResponse.setCode(200);

        return shippingResponse;
    }



}
