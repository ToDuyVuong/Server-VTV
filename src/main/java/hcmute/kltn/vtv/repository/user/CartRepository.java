package hcmute.kltn.vtv.repository.user;

import hcmute.kltn.vtv.model.entity.user.Cart;
import hcmute.kltn.vtv.model.extra.CartStatus;
import hcmute.kltn.vtv.model.extra.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    boolean existsByCartIdAndCustomerUsername(Long cartId, String username);

    boolean existsByProductVariantProductVariantIdAndCustomerUsernameAndStatus(Long productVariantId, String username,
                                                                               CartStatus status);

    Optional<Cart> findByProductVariantProductVariantIdAndCustomerUsernameAndStatus(Long productVariantId,
            String username, CartStatus status);

    Optional<Cart> findByCustomerUsernameAndCartId(String username, Long cartId);



    Optional<List<Cart>> findAllByCustomerUsernameAndStatus(String username, CartStatus status);

    Optional<List<Cart>> findAllByCustomerUsernameAndStatusAndCartIdIn(String username, CartStatus status,
            List<Long> cartIds);

    Optional<List<Cart>> findAllByCustomerUsernameAndProductVariantProductShopShopIdAndStatus(String username,
            Long shopId, CartStatus status);
}
