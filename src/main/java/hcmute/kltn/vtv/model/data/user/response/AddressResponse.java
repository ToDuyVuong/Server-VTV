package hcmute.kltn.vtv.model.data.user.response;

import hcmute.kltn.vtv.model.dto.user.AddressDTO;
import hcmute.kltn.vtv.model.dto.user.CustomerDTO;
import hcmute.kltn.vtv.model.entity.user.Address;
import hcmute.kltn.vtv.model.entity.user.Customer;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse extends ResponseAbstract {

    private AddressDTO addressDTO;
    private CustomerDTO customerDTO;


    public static AddressResponse addressResponse(Address address, Customer customer,
                                                  String message, String status) {
        AddressResponse addressResponse = new AddressResponse();
        addressResponse.setAddressDTO(AddressDTO.convertEntityToDTO(address));
        addressResponse.setCustomerDTO(CustomerDTO.convertEntityToDTO(customer));
        addressResponse.setMessage(message);
        addressResponse.setStatus(status);
        addressResponse.setCode(200);

        return addressResponse;
    }
}
