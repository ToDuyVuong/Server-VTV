package hcmute.kltn.vtv.service.user;

import hcmute.kltn.vtv.model.entity.vendor.Voucher;
import hcmute.kltn.vtv.model.extra.Status;

import java.time.LocalDateTime;
import java.util.Date;

public interface IVoucherCustomerService {
    Long discountVoucherByShopVoucherCodeAndShopIdAndTotalPrice(String shopVoucherCode, Long shopId, Long totalPrice);

    Long discountVoucherBySystemVoucherCodeAndTotalPrice(String systemVoucherCode, Long totalPrice);

    Voucher getVoucherByShopVoucherCodeAndShopId(String shopVoucherCode, Long shopId);

    Voucher getVoucherBySystemVoucherCode(String systemVoucherCode);

    void checkShopVoucherCodeAndShopId(String shopVoucherCode, Long shopId);


    void checkSystemVoucherCode(String systemVoucherCode);

    void checkExpiredVoucher(Date startDate, Date endDate);

    void checkVoucherUsage(int quantityUsed, int quantity);

    void checkVoucherAvailability(Status voucherStatus);
}
