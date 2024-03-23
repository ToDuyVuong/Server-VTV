package hcmute.kltn.vtv.repository.wallet;

import hcmute.kltn.vtv.model.entity.vtv.Brand;
import hcmute.kltn.vtv.model.entity.wallet.LoyaltyPointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface LoyaltyPointHistoryRepository extends JpaRepository<LoyaltyPointHistory, Long> {
    Optional<List<LoyaltyPointHistory>> findByLoyaltyPoint_LoyaltyPointIdAndLoyaltyPoint_Username(Long loyaltyPointId, String username);

}
