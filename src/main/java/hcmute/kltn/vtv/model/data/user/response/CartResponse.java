package hcmute.kltn.vtv.model.data.user.response;

import hcmute.kltn.vtv.model.dto.user.CartDTO;
import hcmute.kltn.vtv.model.entity.user.Cart;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse extends ResponseAbstract {

    private String username;

    private CartDTO cartDTO;


    public static CartResponse cartResponse(String username, Cart cart, String message, String status) {
        CartResponse cartResponse = new CartResponse();
        cartResponse.setUsername(username);
        if (cart != null) {
            cartResponse.setCartDTO(CartDTO.convertEntityToDTO(cart));
        }
        cartResponse.setMessage(message);
        cartResponse.setStatus(status);
        cartResponse.setCode(200);

        return cartResponse;
    }

}
