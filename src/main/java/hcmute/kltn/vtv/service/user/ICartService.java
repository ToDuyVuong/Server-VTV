package hcmute.kltn.vtv.service.user;

import hcmute.kltn.vtv.model.data.user.request.CartRequest;
import hcmute.kltn.vtv.model.data.user.response.CartResponse;
import hcmute.kltn.vtv.model.data.user.response.ListCartResponse;
import hcmute.kltn.vtv.model.entity.user.Cart;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface ICartService {
    @Transactional
    CartResponse addNewCart(CartRequest request);

    @Transactional
    CartResponse updateCart(CartRequest request);

    @Transactional
    CartResponse deleteCart(UUID cartId, String username);

    ListCartResponse getListCartByUsername(String username);

    ListCartResponse getListCartByUsernameAndListCartId(String username, List<UUID> cartIds);

    List<Cart> getListCartByUsernameAndIds(String username, List<UUID> cartIds);

    Cart getCartByUserNameAndId(String username, UUID cartId);

    @Transactional
    ListCartResponse deleteCartByShopId(Long shopId, String username);

    boolean checkCartsSameShop(String username, List<UUID> cartIds);
}
