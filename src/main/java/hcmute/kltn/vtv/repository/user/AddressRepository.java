package hcmute.kltn.vtv.repository.user;

import hcmute.kltn.vtv.model.entity.user.Address;
import hcmute.kltn.vtv.model.entity.user.Customer;
import hcmute.kltn.vtv.model.extra.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {


    boolean existsByAddressIdAndCustomerUsername(Long addressId, String username);


    Optional<List<Address>> findAllByCustomerAndStatusNot(Customer customer, Status status);

    Optional<Address> findFirstByCustomerUsernameAndStatus(String username, Status status);

    Optional<Address> findByAddressIdAndCustomerUsername(Long addressId, String username);

    Optional<List<Address>> findAllByCustomerAndStatus(Customer customer, Status status);

    Optional<List<Address>> findAllByCustomerAndStatusAndAddressIdNot(Customer customer, Status status, Long addressId);
}
