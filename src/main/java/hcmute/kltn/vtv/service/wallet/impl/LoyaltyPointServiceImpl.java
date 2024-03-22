package hcmute.kltn.vtv.service.wallet.impl;

import hcmute.kltn.vtv.model.entity.wallet.LoyaltyPoint;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.wallet.LoyaltyPointRepository;
import hcmute.kltn.vtv.service.user.ICustomerService;
import hcmute.kltn.vtv.service.wallet.ILoyaltyPointHistoryService;
import hcmute.kltn.vtv.service.wallet.ILoyaltyPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LoyaltyPointServiceImpl implements ILoyaltyPointService {

    private final LoyaltyPointRepository loyaltyPointRepository;
    private final ILoyaltyPointHistoryService loyaltyPointHistoryService;
    private final ICustomerService customerService;


    @Async
    @Override
    @Transactional
    public void addNewLoyaltyPointAfterRegister(String username) {
        if(!loyaltyPointRepository.existsByUsername(username)){
            try {
                loyaltyPointRepository.save(createLoyaltyPointByUserame(username));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }





    private LoyaltyPoint createLoyaltyPointByUserame(String username) {
        LoyaltyPoint loyaltyPoint = new LoyaltyPoint();
        loyaltyPoint.setUsername(username);
        loyaltyPoint.setTotalPoint(0L);
        loyaltyPoint.setStatus(Status.ACTIVE);
        loyaltyPoint.setCreateAt(LocalDateTime.now());
        loyaltyPoint.setUpdateAt(LocalDateTime.now());
        loyaltyPoint.setLoyaltyPointHistories(null);

        return loyaltyPoint;
    }



}
