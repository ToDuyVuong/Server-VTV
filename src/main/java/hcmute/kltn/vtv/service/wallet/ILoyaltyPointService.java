package hcmute.kltn.vtv.service.wallet;

import hcmute.kltn.vtv.model.entity.wallet.LoyaltyPoint;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

public interface ILoyaltyPointService {
    @Async
    @Transactional
    void addNewLoyaltyPointAfterRegister(String username);

    @Async
    @Transactional
    void plusLoyaltyPointByUsername(String username, Long point, String type);

    LoyaltyPoint getLoyaltyPointByUsername(String username);
}
