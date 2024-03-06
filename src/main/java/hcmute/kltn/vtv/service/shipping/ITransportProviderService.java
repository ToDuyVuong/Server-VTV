package hcmute.kltn.vtv.service.shipping;

import hcmute.kltn.vtv.model.data.shipping.request.UpdateTransportProviderRequest;
import hcmute.kltn.vtv.model.data.shipping.response.ListTransportProviderResponse;
import hcmute.kltn.vtv.model.data.shipping.response.TransportProviderResponse;
import hcmute.kltn.vtv.model.entity.shipping.TransportProvider;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ITransportProviderService {


    @Transactional
    TransportProviderResponse updateTransportProviderResponse(UpdateTransportProviderRequest request);

    TransportProvider getTransportProviderByShortName(String shortName);

    List<TransportProvider> getTransportProvidersByProvince(String provinceCodeShop, String provinceCodeCustomer);

    TransportProviderResponse getTransportProviderById(Long id);

    TransportProvider getTransportProviderByTransportProviderId(Long id);

    ListTransportProviderResponse getAllTransportProvidersNotProvince();

    ListTransportProviderResponse getAllTransportProviders();




}
