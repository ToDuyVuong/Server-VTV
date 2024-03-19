package hcmute.kltn.vtv.service.guest;

import hcmute.kltn.vtv.model.data.guest.ListVoucherResponse;
import hcmute.kltn.vtv.model.data.guest.VoucherResponse;
import hcmute.kltn.vtv.model.entity.vendor.Voucher;
import hcmute.kltn.vtv.model.extra.VoucherType;

import java.util.List;

public interface IVoucherService {
    VoucherResponse getVoucherByVoucherId(Long voucherId);

    ListVoucherResponse listVoucherByShopId(Long shopId);

    ListVoucherResponse listVoucherByType(VoucherType type);

    ListVoucherResponse listVoucherSystem();

    ListVoucherResponse allVoucher();

    VoucherResponse voucherResponse(Voucher voucher);

    ListVoucherResponse listVoucherResponse(List<Voucher> vouchers, String message);

    Voucher getVoucherById(Long voucherId);
}
