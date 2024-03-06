package hcmute.kltn.vtv.model.data.shipping.response;

import hcmute.kltn.vtv.model.dto.shipping.TransportProviderDTO;
import hcmute.kltn.vtv.model.entity.shipping.TransportProvider;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TransportProviderResponse extends ResponseAbstract {

    private TransportProviderDTO transportProviderDTO;


    public static TransportProviderResponse transportProviderResponse(TransportProvider transportProvider, String meesage, String status) {
        TransportProviderResponse response = new TransportProviderResponse();
        response.setTransportProviderDTO(TransportProviderDTO.convertEntityToDTO(transportProvider));
        response.setMessage(meesage);
        response.setStatus(status);
        response.setCode(200);
        return response;
    }
}
