package hcmute.kltn.vtv.service.vendor;

import hcmute.kltn.vtv.model.data.vendor.request.VoucherShopRequest;
import hcmute.kltn.vtv.model.data.vendor.response.ListVoucherShopResponse;
import hcmute.kltn.vtv.model.data.vendor.response.VoucherShopResponse;
import hcmute.kltn.vtv.model.entity.vendor.Voucher;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.model.extra.VoucherType;
import org.springframework.transaction.annotation.Transactional;

public interface IVoucherShopService {
    @Transactional
    VoucherShopResponse addNewVoucher(VoucherShopRequest request, String username);

    VoucherShopResponse getVoucherByVoucherId(Long voucherId, String username);

    ListVoucherShopResponse getListVoucherByUsername(String username);

    ListVoucherShopResponse getListVoucherByUsernameAndStatus(String username, Status status);

    ListVoucherShopResponse getListVoucherByUsernameAndVoucherType(String username, VoucherType type);

    @Transactional
    VoucherShopResponse updateVoucher(VoucherShopRequest request, String username);

    @Transactional
    VoucherShopResponse updateStatusVoucher(Long voucherId, Status status, String username);

    Voucher checkVoucherShop(Long voucherId, Long shopId);
}
