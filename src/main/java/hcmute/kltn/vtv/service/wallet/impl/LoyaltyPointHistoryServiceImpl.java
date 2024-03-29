package hcmute.kltn.vtv.service.wallet.impl;

import hcmute.kltn.vtv.model.data.wallet.response.LoyaltyPointHistoriesResponse;
import hcmute.kltn.vtv.model.entity.wallet.LoyaltyPoint;
import hcmute.kltn.vtv.model.entity.wallet.LoyaltyPointHistory;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.wallet.LoyaltyPointHistoryRepository;
import hcmute.kltn.vtv.repository.wallet.LoyaltyPointRepository;
import hcmute.kltn.vtv.service.wallet.ILoyaltyPointHistoryService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class LoyaltyPointHistoryServiceImpl implements ILoyaltyPointHistoryService {


    private final LoyaltyPointHistoryRepository loyaltyPointHistoryRepository;
    private final LoyaltyPointRepository loyaltyPointRepository;





    @Override
    @Transactional
    public LoyaltyPointHistory addNewLoyaltyPointHistory(LoyaltyPoint loyaltyPoint, Long point, String type) {
        LoyaltyPointHistory loyaltyPointHistory = createLoyaltyPointHistory(loyaltyPoint, point, type);
        try {

            return loyaltyPointHistoryRepository.save(loyaltyPointHistory);
        } catch (Exception e) {
            throw new InternalServerErrorException("Thêm mới lịch sử điểm thưởng thất bại");
        }
    }


    @Override
    public LoyaltyPointHistoriesResponse getLoyaltyPointHistoriesResponseByLoyaltyPointId(Long loyaltyPointId, String username) {
        checkExistLoyaltyPointByIdAndUsername(loyaltyPointId, username);
        List<LoyaltyPointHistory> loyaltyPointHistories = loyaltyPointHistoryRepository.findByLoyaltyPoint_LoyaltyPointIdAndLoyaltyPoint_Username(loyaltyPointId, username)
                .orElseThrow(() -> new NotFoundException("Danh sách lịch sử điểm thưởng không tồn tại"));
        return LoyaltyPointHistoriesResponse.loyaltyPointHistoriesResponse(loyaltyPointHistories, "Lấy danh sách lịch sử điểm thưởng thành công", "OK");
    }


    private LoyaltyPointHistory createLoyaltyPointHistory(LoyaltyPoint loyaltyPoint, Long point, String type) {
        LoyaltyPointHistory loyaltyPointHistory = new LoyaltyPointHistory();
        loyaltyPointHistory.setLoyaltyPoint(loyaltyPoint);
        loyaltyPointHistory.setPoint(point);
        loyaltyPointHistory.setType(type);
        loyaltyPointHistory.setStatus(Status.ACTIVE);
        loyaltyPointHistory.setCreateAt(LocalDateTime.now());

        return loyaltyPointHistory;
    }


    private void checkExistLoyaltyPointByIdAndUsername(Long loyaltyPointId, String username) {
        if (!loyaltyPointRepository.existsByLoyaltyPointIdAndUsername(loyaltyPointId, username)) {
            throw new BadRequestException("Mã điểm tích lũy không tồn tại hoặc không thuộc tài khoản " + username);
        }
    }
}

