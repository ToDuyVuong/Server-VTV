package hcmute.kltn.vtv.service.user.impl;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.entity.user.Order;
import hcmute.kltn.vtv.model.entity.vendor.Voucher;
import hcmute.kltn.vtv.model.entity.user.VoucherOrder;
import hcmute.kltn.vtv.model.extra.VoucherType;
import hcmute.kltn.vtv.repository.user.VoucherOrderRepository;
import hcmute.kltn.vtv.repository.vtv.VoucherRepository;
import hcmute.kltn.vtv.service.admin.impl.VoucherAdminServiceImpl;
import hcmute.kltn.vtv.service.user.IVoucherOrderService;
import hcmute.kltn.vtv.service.vendor.impl.VoucherShopServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VoucherOrderServiceImpl implements IVoucherOrderService {

    @Autowired
    private final VoucherOrderRepository voucherOrderRepository;
    @Autowired
    private final VoucherRepository voucherRepository;
    @Autowired
    private final VoucherShopServiceImpl voucherShopService;
    @Autowired
    private final VoucherAdminServiceImpl voucherSystemService;

    @Transactional
    @Override
    public VoucherOrder saveVoucherOrder(Long voucherId, Order order, boolean isShop) {
        Voucher voucher;
        if (isShop) {
            voucher = voucherShopService.checkVoucherShop(voucherId, order.getShopId());

        } else {
            voucher = voucherSystemService.checkVoucherSystem(voucherId);
        }
        VoucherOrder voucherOrder = new VoucherOrder();
        voucherOrder.setType(!isShop);
        voucherOrder.setVoucher(voucher);
        voucherOrder.setOrder(order);
        try {
            voucher.setQuantityUsed(voucher.getQuantityUsed() + 1);
            voucherRepository.save(voucher);
            return voucherOrderRepository.save(voucherOrder);
        } catch (Exception e) {
            throw new BadRequestException("Thêm mới mã giảm giá thất bại!");
        }
    }

    @Transactional
    @Override
    public VoucherOrder cancelVoucherOrder(Long voucherOrderId) {
        VoucherOrder voucherOrder = voucherOrderRepository.findById(voucherOrderId)
                .orElseThrow(() -> new BadRequestException("Mã giảm giá không tồn tại!"));

        Voucher voucher = voucherOrder.getVoucher();
        voucher.setQuantityUsed(voucher.getQuantityUsed() - 1);

        try {
            voucherRepository.save(voucher);
            return voucherOrder;
        } catch (Exception e) {
            throw new BadRequestException("Cập nhật mã giảm giá thất bại!");
        }
    }

    @Override
    public int calculateVoucher(Long voucherId, Long shopId, Long totalPrice, boolean isShop) {
        Voucher voucher;
        if (isShop) {
            voucher = voucherShopService.checkVoucherShop(voucherId, shopId);
        } else {
            voucher = voucherSystemService.checkVoucherSystem(voucherId);
        }

        if (voucher.getType().equals(VoucherType.SHIPPING)) {
            return voucher.getDiscount();
        }

        if (voucher.getType().equals(VoucherType.PERCENTAGE_SHOP)
                || voucher.getType().equals(VoucherType.PERCENTAGE_SYSTEM)) {
            return (int) (voucher.getDiscount() * totalPrice / 100);
        }

        return voucher.getDiscount();
    }

}
