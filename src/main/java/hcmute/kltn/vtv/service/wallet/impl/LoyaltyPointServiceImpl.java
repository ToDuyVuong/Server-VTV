package hcmute.kltn.vtv.service.wallet.impl;

import hcmute.kltn.vtv.repository.wallet.LoyaltyPointRepository;
import hcmute.kltn.vtv.service.user.ICustomerService;
import hcmute.kltn.vtv.service.wallet.ILoyaltyPointHistoryService;
import hcmute.kltn.vtv.service.wallet.ILoyaltyPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoyaltyPointServiceImpl implements ILoyaltyPointService {

    @Autowired
    private final LoyaltyPointRepository loyaltyPointRepository;
    @Autowired
    private final ILoyaltyPointHistoryService loyaltyPointHistoryService;

    private final ICustomerService customerService;

}
