package hcmute.kltn.vtv.service.wallet;

import hcmute.kltn.vtv.model.data.wallet.response.LoyaltyPointHistoriesResponse;
import hcmute.kltn.vtv.model.entity.wallet.LoyaltyPoint;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

public interface ILoyaltyPointHistoryService {
    @Async
    @Transactional
    void addNewLoyaltyPointHistoryByLoyaltyPointId(LoyaltyPoint loyaltyPoint, Long point, String type, boolean earned);

    LoyaltyPointHistoriesResponse getLoyaltyPointHistoriesResponseByLoyaltyPointId(Long loyaltyPointId, String username);
}
