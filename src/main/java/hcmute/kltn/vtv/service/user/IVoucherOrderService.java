package hcmute.kltn.vtv.service.user;

import hcmute.kltn.vtv.model.entity.user.Order;
import hcmute.kltn.vtv.model.entity.user.VoucherOrder;
import org.springframework.transaction.annotation.Transactional;

public interface IVoucherOrderService {

    @Transactional
    VoucherOrder saveVoucherOrder(Long voucherId, Order order, boolean isShop);

    @Transactional
    VoucherOrder addNewVoucherOrderByCode(String code, Order order, Long shopId);

    @Transactional
    VoucherOrder cancelVoucherOrder(Long voucherOrderId);

    /*
     * @Transactional
     * VoucherOrder cancelVoucherOrder(Long voucherOrderId, boolean isShop);
     */


    int calculateVoucher(Long voucherId, Long shopId, Long totalPrice, boolean isShop);

    VoucherOrder createVoucherOrder(String voucherCode, Long shopId);
}
