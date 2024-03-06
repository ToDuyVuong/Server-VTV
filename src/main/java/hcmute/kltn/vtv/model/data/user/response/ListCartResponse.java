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

    private String username;

    private int count;

    private List<ListCartByShopDTO> listCartByShopDTOs;

    public static ListCartResponse listCartResponse(String username, List<Cart> carts,
                                                    String message, String status) {
        ListCartResponse response = new ListCartResponse();
        response.setListCartByShopDTOs(ListCartByShopDTO.convertToListDTOByShop(carts));
        response.setCount(carts.size());
        response.setUsername(username);
        response.setStatus(status);
        response.setMessage(message);
        response.setCode(200);
        return response;
    }
}
