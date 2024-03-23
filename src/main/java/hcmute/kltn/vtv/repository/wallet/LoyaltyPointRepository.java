package hcmute.kltn.vtv.repository.wallet;

import hcmute.kltn.vtv.model.entity.wallet.LoyaltyPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoyaltyPointRepository extends JpaRepository<LoyaltyPoint, Long>{

    boolean existsByUsername(String username);

    boolean existsByLoyaltyPointIdAndUsername(Long loyaltyPointId, String username);


    Optional<LoyaltyPoint> findByUsername(String username);
}
