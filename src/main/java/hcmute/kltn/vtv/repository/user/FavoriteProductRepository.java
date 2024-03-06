package hcmute.kltn.vtv.repository.user;

import hcmute.kltn.vtv.model.entity.user.Customer;
import hcmute.kltn.vtv.model.entity.user.FavoriteProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteProductRepository extends JpaRepository<FavoriteProduct, Long> {

    Optional<List<FavoriteProduct>> findByCustomer(Customer customer);

    boolean existsByCustomerUsernameAndProductProductId(String username, Long productId);

    int countByProductProductId(Long productId);

    boolean existsByFavoriteProductIdAndCustomerUsername(Long favoriteProductId, String username);
}
