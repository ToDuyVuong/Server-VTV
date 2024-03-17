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

    private CartDTO cartDTO;


    public static CartResponse cartResponse(Cart cart, String message, String status) {
        CartResponse cartResponse = new CartResponse();
        cartResponse.setCartDTO(cart != null ? CartDTO.convertEntityToDTO(cart) : null);
        cartResponse.setMessage(message);
        cartResponse.setStatus(status);
        cartResponse.setCode(200);

        return cartResponse;
    }


    public static CartResponse cartResponseDelete(String message, String status) {
        CartResponse cartResponse = new CartResponse();
        cartResponse.setMessage(message);
        cartResponse.setStatus(status);
        cartResponse.setCode(200);

        return cartResponse;
    }

}
