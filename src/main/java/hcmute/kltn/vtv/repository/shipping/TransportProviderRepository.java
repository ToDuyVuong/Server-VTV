package hcmute.kltn.vtv.repository.shipping;

import hcmute.kltn.vtv.model.entity.shipping.TransportProvider;
import hcmute.kltn.vtv.model.extra.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransportProviderRepository extends JpaRepository<TransportProvider, Long> {

    boolean existsByTransportProviderId(Long transportProviderId);

    Optional<List<TransportProvider>> findAllByStatus(Status status);

    Optional<TransportProvider> findByTransportProviderId(Long transportProviderId);

    Optional<TransportProvider> findByEmail(String email);

    Optional<TransportProvider> findByPhone(String phone);

    Optional<TransportProvider> findByCustomerUsername(String username);

    Optional<TransportProvider> findByShortName(String shortName);





    @Query("SELECT tp " +
            "FROM TransportProvider tp " +
            "JOIN tp.provinces p1 " +
            "JOIN tp.provinces p2 " +
            "WHERE p1.provinceCode = :provinceCodeShop " +
            "AND p2.provinceCode = :provinceCodeCustomer " +
            "AND tp.status = :status")
    Optional<List<TransportProvider>> findAllByProvinceCodeShopAndProvinceCodeCustomerAndStatus(
            String provinceCodeShop, String provinceCodeCustomer, Status status);


}
