package hcmute.kltn.vtv.model.data.user.response;

import hcmute.kltn.vtv.model.dto.user.ListCartByShopDTO;
import hcmute.kltn.vtv.model.entity.user.Cart;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ListCartResponse extends ResponseAbstract {

    private int count;

    private List<ListCartByShopDTO> listCartByShopDTOs;

    public static ListCartResponse listCartResponse(List<Cart> carts,
                                                    String message, String status) {
        ListCartResponse response = new ListCartResponse();
        response.setListCartByShopDTOs(ListCartByShopDTO.convertEntitiesToDTOsByShop(carts));
        response.setCount(carts.size());
        response.setStatus(status);
        response.setMessage(message);
        response.setCode(200);
        return response;
    }


    public static ListCartResponse listCartResponse(String message, String status) {
        ListCartResponse response = new ListCartResponse();
        response.setStatus(status);
        response.setMessage(message);
        response.setCode(200);
        return response;
    }
}
