package hcmute.kltn.vtv.service.wallet;

import hcmute.kltn.vtv.model.data.wallet.response.LoyaltyPointHistoriesResponse;
import hcmute.kltn.vtv.model.entity.wallet.LoyaltyPoint;
import hcmute.kltn.vtv.model.entity.wallet.LoyaltyPointHistory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

public interface ILoyaltyPointHistoryService {

    @Transactional
    LoyaltyPointHistory addNewLoyaltyPointHistory(LoyaltyPoint loyaltyPoint, Long point, String type);

    LoyaltyPointHistoriesResponse getLoyaltyPointHistoriesResponseByLoyaltyPointId(Long loyaltyPointId, String username);
}
