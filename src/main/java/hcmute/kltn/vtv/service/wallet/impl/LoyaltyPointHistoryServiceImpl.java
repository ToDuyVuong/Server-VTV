package hcmute.kltn.vtv.service.wallet.impl;

import hcmute.kltn.vtv.model.entity.wallet.LoyaltyPoint;
import hcmute.kltn.vtv.model.entity.wallet.LoyaltyPointHistory;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.wallet.LoyaltyPointHistoryRepository;
import hcmute.kltn.vtv.service.wallet.ILoyaltyPointHistoryService;
import hcmute.kltn.vtv.service.wallet.ILoyaltyPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class LoyaltyPointHistoryServiceImpl implements ILoyaltyPointHistoryService {


    @Autowired
    private final LoyaltyPointHistoryRepository loyaltyPointHistoryRepository;



    @Async
    @Override
    @Transactional
    public void addNewLoyaltyPointHistoryByLoyaltyPointId(LoyaltyPoint loyaltyPoint, Long point, String type, boolean earned) {
        LoyaltyPointHistory loyaltyPointHistory = createLoyaltyPointHistory(loyaltyPoint, point, type, earned);

        try {
            loyaltyPointHistoryRepository.save(loyaltyPointHistory);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    private LoyaltyPointHistory createLoyaltyPointHistory(LoyaltyPoint loyaltyPoint, Long point, String type, boolean earned) {
        LoyaltyPointHistory loyaltyPointHistory = new LoyaltyPointHistory();
        loyaltyPointHistory.setLoyaltyPoint(loyaltyPoint);
        loyaltyPointHistory.setPoint(point);
        loyaltyPointHistory.setType(type);
        loyaltyPointHistory.setEarned(earned);
        loyaltyPointHistory.setStatus(Status.ACTIVE);
        loyaltyPointHistory.setCreateAt(LocalDateTime.now());

        return loyaltyPointHistory;
    }
}

