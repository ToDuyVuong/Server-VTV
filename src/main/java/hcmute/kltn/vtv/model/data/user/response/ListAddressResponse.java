package hcmute.kltn.vtv.model.data.user.response;

import hcmute.kltn.vtv.model.dto.user.AddressDTO;
import hcmute.kltn.vtv.model.dto.user.CustomerDTO;
import hcmute.kltn.vtv.model.entity.user.Address;
import hcmute.kltn.vtv.model.entity.user.Customer;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ListAddressResponse extends ResponseAbstract {

    private List<AddressDTO> addressDTOs;
    private CustomerDTO customerDTO;


    public static ListAddressResponse listAddressResponse(List<Address> addresses, Customer customer,
                                                         String message, String status) {
        ListAddressResponse listAddressResponse = new ListAddressResponse();
        listAddressResponse.setAddressDTOs(AddressDTO.convertEntitiesToDTOs(addresses));
        listAddressResponse.setCustomerDTO(CustomerDTO.convertEntityToDTO(customer));
        listAddressResponse.setMessage(message);
        listAddressResponse.setStatus(status);
        listAddressResponse.setCode(200);

        return listAddressResponse;
    }
}
