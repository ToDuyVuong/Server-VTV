package hcmute.kltn.vtv.service.shipping;

import hcmute.kltn.vtv.model.data.shipping.response.ListShippingResponse;
import hcmute.kltn.vtv.model.data.shipping.response.ShippingResponse;

public interface IShippingService {
    ShippingResponse getCalculateShippingByWardAndTransportProvider(String wardCodeCustomer, String wardCodeShop, String shippingProvider);

    ListShippingResponse getShippingProvidersByWard(String wardCodeCustomer, String wardCodeShop);

    void checkShippingProvider(String shippingProvider);
}
