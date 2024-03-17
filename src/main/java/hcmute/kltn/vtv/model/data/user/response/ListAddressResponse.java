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

    private String username;
    private int count;
    private List<AddressDTO> addressDTOs;


    public static ListAddressResponse listAddressResponse(List<Address> addresses, String username,
                                                         String message, String status) {
        ListAddressResponse listAddressResponse = new ListAddressResponse();
        listAddressResponse.setUsername(username);
        listAddressResponse.setCount(addresses.size());
        listAddressResponse.setAddressDTOs(AddressDTO.convertEntitiesToDTOs(addresses));
        listAddressResponse.setMessage(message);
        listAddressResponse.setStatus(status);
        listAddressResponse.setCode(200);

        return listAddressResponse;
    }
}
