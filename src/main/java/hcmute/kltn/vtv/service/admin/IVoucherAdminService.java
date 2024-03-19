package hcmute.kltn.vtv.service.admin;

import hcmute.kltn.vtv.model.data.admin.request.VoucherAdminRequest;
import hcmute.kltn.vtv.model.data.admin.response.ListVoucherAdminResponse;
import hcmute.kltn.vtv.model.data.admin.response.VoucherAdminResponse;
import hcmute.kltn.vtv.model.entity.vendor.Voucher;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.model.extra.VoucherType;
import org.springframework.transaction.annotation.Transactional;

public interface IVoucherAdminService {
    VoucherAdminResponse addNewVoucherAdmin(VoucherAdminRequest request);

    VoucherAdminResponse getVoucherAdminByVoucherId(Long voucherId);

    ListVoucherAdminResponse getListVoucherAdmin(String username);

    ListVoucherAdminResponse getListVoucherAdminByUsername(String username);

    ListVoucherAdminResponse getListVoucherAdminByStatus(String username, Status status);

    ListVoucherAdminResponse getListVoucherAdminByType(String username, VoucherType type);

    @Transactional
    VoucherAdminResponse updateVoucherAdmin(VoucherAdminRequest request, String username);

    @Transactional
    VoucherAdminResponse updateStatusVoucherAdmin(Long voucherId, Status status, String username);

    Voucher checkVoucherSystem(Long voucherId);
}
