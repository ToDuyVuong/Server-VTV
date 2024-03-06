package hcmute.kltn.vtv.service.user;

import hcmute.kltn.vtv.model.data.user.request.AddressRequest;
import hcmute.kltn.vtv.model.data.user.request.AddressStatusRequest;
import hcmute.kltn.vtv.model.data.user.response.AddressResponse;
import hcmute.kltn.vtv.model.data.user.response.ListAddressResponse;
import hcmute.kltn.vtv.model.entity.user.Address;

public interface IAddressService {
    AddressResponse addNewAddress(AddressRequest request);

    AddressResponse getAddressById(Long addressId, String username);

    AddressResponse updateAddress(AddressRequest request);

    AddressResponse updateStatusAddress(AddressStatusRequest request);

    ListAddressResponse getAllAddress(String username);

    Address getAddressActiveByUsername(String username);



    Address checkAddress(Long addressId, String username);
}
