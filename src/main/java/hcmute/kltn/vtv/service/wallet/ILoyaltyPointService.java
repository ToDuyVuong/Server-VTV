package hcmute.kltn.vtv.service.wallet;

import hcmute.kltn.vtv.model.data.wallet.response.LoyaltyPointResponse;
import hcmute.kltn.vtv.model.entity.wallet.LoyaltyPoint;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

public interface ILoyaltyPointService {
    @Async
    @Transactional
    void addNewLoyaltyPointAfterRegister(String username);

    @Async
    @Transactional
    void updatePointInLoyaltyPointByUsername(String username, Long point, String type);

    @Async
    @Transactional
    void subtractLoyaltyPointByUsername(String username, Long point, String type);

    LoyaltyPointResponse getLoyaltyPointResponseByUsername(String username);

    LoyaltyPoint getLoyaltyPointByUsername(String username);

    void checkExistLoyaltyPointByIdAndUsername(Long loyaltyPointId, String username);
}
