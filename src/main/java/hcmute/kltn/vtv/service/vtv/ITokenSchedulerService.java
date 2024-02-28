package hcmute.kltn.vtv.service.vtv;

import org.springframework.transaction.annotation.Transactional;

public interface ITokenSchedulerService {
    @Transactional
    void checkExpirationToken();

    @Transactional
    void deleteTokenExpiredAndRevoked();
}
