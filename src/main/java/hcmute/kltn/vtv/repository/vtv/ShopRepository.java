package hcmute.kltn.vtv.repository.vtv;

import hcmute.kltn.vtv.model.entity.vendor.Shop;
import hcmute.kltn.vtv.model.extra.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {

    Shop findByEmail(String email);

    Shop findByPhone(String phone);

    Shop findByCustomer_Username(String username);

    Optional<Shop> findByCustomerUsername(String username);

    boolean existsByName(String name);

    boolean existsByNameAndCustomerUsernameNot(String name, String username);

    boolean existsByEmail(String email);

    boolean existsByEmailAndCustomerUsernameNot(String email, String username);

    boolean existsByPhone(String phone);

    boolean existsByPhoneAndCustomerUsernameNot(String phone, String username);

    boolean existsByCustomerUsername(String username);

    int countAllByStatus(Status status);

    Optional<Page<Shop>> findAllByStatus(Status status, Pageable pageable);

    int countByNameContainsAndStatus(String name, Status status);

    Optional<Page<Shop>> findAllByNameContainsAndStatusOrderByName(String name, Status status, Pageable pageable);

}
