package hcmute.kltn.vtv.model.data.user.response;

import hcmute.kltn.vtv.model.dto.user.CustomerDTO;
import hcmute.kltn.vtv.model.dto.user.FavoriteProductDTO;
import hcmute.kltn.vtv.model.entity.user.Customer;
import hcmute.kltn.vtv.model.entity.user.FavoriteProduct;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ListFavoriteProductResponse extends ResponseAbstract {

    int count;

    private CustomerDTO customerDTO;

    private List<FavoriteProductDTO> favoriteProductDTOs;


    public static ListFavoriteProductResponse listFavoriteProductResponse(Customer customer, List<FavoriteProduct> favoriteProducts,
                                                                          String message, String status) {
        ListFavoriteProductResponse response = new ListFavoriteProductResponse();
        response.setCustomerDTO(CustomerDTO.convertEntityToDTO(customer));
        response.setFavoriteProductDTOs(FavoriteProductDTO.convertToEntitiesDTOs(favoriteProducts));
        response.setCount(favoriteProducts.size());
        response.setStatus(status);
        response.setMessage(message);
        response.setCode(200);
        return response;
    }

}
