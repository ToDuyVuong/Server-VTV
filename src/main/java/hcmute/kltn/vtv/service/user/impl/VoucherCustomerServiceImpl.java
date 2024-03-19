package hcmute.kltn.vtv.service.user.impl;

import hcmute.kltn.vtv.model.entity.vendor.Voucher;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.model.extra.VoucherType;
import hcmute.kltn.vtv.repository.vtv.VoucherRepository;
import hcmute.kltn.vtv.service.user.IVoucherCustomerService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


@Service
@RequiredArgsConstructor
public class VoucherCustomerServiceImpl implements IVoucherCustomerService {
    @Autowired
    private final VoucherRepository voucherRepository;



    @Override
    public Long discountVoucherByShopVoucherCodeAndShopIdAndTotalPrice(String shopVoucherCode, Long shopId, Long totalPrice) {
        Voucher voucher = getVoucherByShopVoucherCodeAndShopId(shopVoucherCode, shopId);

        if (voucher.getType().equals(VoucherType.PERCENTAGE_SHOP)) {
            return totalPrice * voucher.getDiscount() / 100;
        }

        return (long) voucher.getDiscount();
    }


    @Override
    public Long discountVoucherBySystemVoucherCodeAndTotalPrice(String systemVoucherCode, Long totalPrice) {
        Voucher voucher = getVoucherBySystemVoucherCode(systemVoucherCode);

        if (voucher.getType().equals(VoucherType.PERCENTAGE_SYSTEM)) {
            return totalPrice * voucher.getDiscount() / 100;
        }

        return (long) voucher.getDiscount();
    }




    @Override
    public Voucher getVoucherByShopVoucherCodeAndShopId(String shopVoucherCode, Long shopId) {
        checkShopVoucherCodeAndShopId(shopVoucherCode, shopId);
        Voucher voucher = voucherRepository.findByCodeAndShopShopId(shopVoucherCode, shopId)
                .orElseThrow(() -> new BadRequestException("Mã giảm giá cửa hàng không tồn tại!"));

        checkExpiredVoucher(voucher.getStartDate(), voucher.getEndDate());
        checkVoucherUsage(voucher.getQuantityUsed(), voucher.getQuantity());
        checkVoucherAvailability(voucher.getStatus());

        return voucher;
    }


    @Override
    public Voucher getVoucherBySystemVoucherCode(String systemVoucherCode) {
        checkSystemVoucherCode(systemVoucherCode);
        Voucher voucher = voucherRepository.findByCode(systemVoucherCode)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy mã giảm giá hệ thống có mã: " + systemVoucherCode));
        checkExpiredVoucher(voucher.getStartDate(), voucher.getEndDate());
        checkVoucherUsage(voucher.getQuantityUsed(), voucher.getQuantity());
        checkVoucherAvailability(voucher.getStatus());
        return voucher;
    }


    @Override
    public void checkShopVoucherCodeAndShopId(String shopVoucherCode, Long shopId) {
        if (!voucherRepository.existsByCodeAndShopShopId(shopVoucherCode, shopId)) {
            throw new BadRequestException("Mã giảm giá cửa hàng không tồn tại!");
        }
    }

    @Override
    public void checkSystemVoucherCode(String systemVoucherCode) {
        if (!voucherRepository.existsByCodeAndShopNull(systemVoucherCode)) {
            throw new BadRequestException("Mã giảm giá hệ thống không tồn tại!");
        }
    }


    @Override
    public void checkExpiredVoucher(Date startDate, Date endDate) {
        LocalDateTime localDateTime = LocalDateTime.now();
        Date currentDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        if (currentDate.before(startDate)) {
            throw new BadRequestException("Voucher chưa có hiệu lực!");
        }
        if (currentDate.after(endDate)) {
            throw new BadRequestException("Voucher đã hết hạn!");
        }


    }

    @Override
    public void checkVoucherUsage(int quantityUsed, int quantity) {
        if (quantityUsed >= quantity) {
            throw new BadRequestException("Voucher đã được sử dụng hết!");
        }
    }

    @Override
    public void checkVoucherAvailability(Status voucherStatus) {
        if (!voucherStatus.equals(Status.ACTIVE)) {
            throw new BadRequestException("Voucher không còn hiệu lực!");
        }
    }


}
