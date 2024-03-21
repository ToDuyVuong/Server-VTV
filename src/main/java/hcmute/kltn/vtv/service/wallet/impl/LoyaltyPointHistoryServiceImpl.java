package hcmute.kltn.vtv.service.wallet.impl;

import hcmute.kltn.vtv.repository.wallet.LoyaltyPointHistoryRepository;
import hcmute.kltn.vtv.service.wallet.ILoyaltyPointHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class LoyaltyPointHistoryServiceImpl implements ILoyaltyPointHistoryService {


    @Autowired
    private final LoyaltyPointHistoryRepository loyaltyPointHistoryRepository;

}

