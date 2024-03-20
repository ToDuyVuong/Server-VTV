package hcmute.kltn.vtv.service.vtv;

import org.springframework.transaction.annotation.Transactional;

public interface IOrderSchedulerService {
    @Transactional
    void autoCompleteOrderAfterFiveDayDelivered();
}
