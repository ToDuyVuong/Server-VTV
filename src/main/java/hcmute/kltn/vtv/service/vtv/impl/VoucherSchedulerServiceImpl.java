package hcmute.kltn.vtv.service.vtv.impl;

import hcmute.kltn.vtv.model.entity.vendor.Voucher;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.vtv.VoucherRepository;
import hcmute.kltn.vtv.service.vtv.IVoucherSchedulerService;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
public class VoucherSchedulerServiceImpl implements IVoucherSchedulerService {

    @Autowired
    private final VoucherRepository voucherRepository;

    //findAllByShopNullAndStatusAndStartDateBeforeAndEndDateAfter


    @Override
    @Transactional
    public void checkExpirationVoucher() {
        List<Voucher> vouchers = voucherRepository.findAllByStatus(Status.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy mã giảm giá!"));

        updateVoucherStatus(vouchers);


    }

    private void updateVoucherStatus(List<Voucher> vouchers) {
        for (Voucher voucher : vouchers) {
            if (voucher.getEndDate().before(new Date())) {
                voucher.setStatus(Status.INACTIVE);
                voucher.setUpdateAt(LocalDateTime.now());

                try {
                    voucherRepository.save(voucher);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
