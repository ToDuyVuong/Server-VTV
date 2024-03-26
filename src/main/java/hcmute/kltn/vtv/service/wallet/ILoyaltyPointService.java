package hcmute.kltn.vtv.service.wallet;

import hcmute.kltn.vtv.model.data.wallet.response.LoyaltyPointResponse;
import hcmute.kltn.vtv.model.entity.wallet.LoyaltyPoint;
import hcmute.kltn.vtv.model.entity.wallet.LoyaltyPointHistory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

public interface ILoyaltyPointService {
    @Async
    @Transactional
    void addNewLoyaltyPointAfterRegister(String username);


    @Transactional
    LoyaltyPointHistory updatePointInLoyaltyPointByUsername(String username, Long point, String type);



    LoyaltyPointResponse getLoyaltyPointResponseByUsername(String username);

    LoyaltyPoint getLoyaltyPointByUsername(String username);

    void checkExistLoyaltyPointByIdAndUsername(Long loyaltyPointId, String username);
}
