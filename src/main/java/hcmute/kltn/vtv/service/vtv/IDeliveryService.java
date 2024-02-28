package hcmute.kltn.vtv.service.vtv;

import hcmute.kltn.vtv.model.data.shipping.response.ShippingResponse;

public interface IDeliveryService {
    ShippingResponse calculateShipping(String wardCodeCustomer, String wardCodeShop, String shippingProvider);
}
