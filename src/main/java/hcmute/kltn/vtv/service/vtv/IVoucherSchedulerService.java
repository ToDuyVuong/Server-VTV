package hcmute.kltn.vtv.service.vtv;

import org.springframework.transaction.annotation.Transactional;

public interface IVoucherSchedulerService {
    @Transactional
    void checkExpirationVoucher();
}
