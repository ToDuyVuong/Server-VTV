package hcmute.kltn.vtv.model.data.user.response;

import hcmute.kltn.vtv.model.dto.user.CustomerDTO;
import hcmute.kltn.vtv.model.entity.user.Customer;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProfileCustomerResponse extends ResponseAbstract {

    private CustomerDTO customerDTO;


    public static ProfileCustomerResponse profileCustomerResponse(Customer customer, String message, String status) {
        ProfileCustomerResponse response = new ProfileCustomerResponse();
        response.setCustomerDTO(CustomerDTO.convertEntityToDTO(customer));
        response.setMessage(message);
        response.setStatus(status);
        response.setCode(200);

        return response;
    }

}
