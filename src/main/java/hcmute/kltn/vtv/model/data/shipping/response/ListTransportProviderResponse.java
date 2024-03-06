package hcmute.kltn.vtv.model.data.shipping.response;

import hcmute.kltn.vtv.model.dto.shipping.TransportProviderDTO;
import hcmute.kltn.vtv.model.entity.shipping.TransportProvider;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ListTransportProviderResponse extends ResponseAbstract {

    private int count;

    private List<TransportProviderDTO> transportProviderDTOs;


    public static ListTransportProviderResponse listTransportProvidersNotProvinceResponse(List<TransportProvider> transportProviders) {
        ListTransportProviderResponse response = new ListTransportProviderResponse();
        response.setTransportProviderDTOs(TransportProviderDTO.convertNotProvinceEntitiesToDTOs(transportProviders));
        response.setCount(response.getTransportProviderDTOs().size());
        response.setMessage("Lấy danh sách nhà vận chuyển thành công.");
        response.setStatus("OK");
        response.setCode(200);
        return response;
    }





    public static ListTransportProviderResponse listTransportProvidersResponse(List<TransportProvider> transportProviders) {
        ListTransportProviderResponse response = new ListTransportProviderResponse();
        response.setTransportProviderDTOs(TransportProviderDTO.convertEntitiesToDTOs(transportProviders));
        response.setCount(response.getTransportProviderDTOs().size());
        response.setMessage("Lấy danh sách nhà vận chuyển thành công.");
        response.setStatus("OK");
        response.setCode(200);
        return response;
    }
}
