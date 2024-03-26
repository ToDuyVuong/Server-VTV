package hcmute.kltn.vtv.service.guest.impl;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.guest.ListVoucherResponse;
import hcmute.kltn.vtv.model.data.guest.VoucherResponse;
import hcmute.kltn.vtv.model.dto.vtv.VoucherDTO;
import hcmute.kltn.vtv.model.entity.vendor.Voucher;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.model.extra.VoucherType;
import hcmute.kltn.vtv.repository.vtv.VoucherRepository;
import hcmute.kltn.vtv.service.guest.IVoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements IVoucherService {

    @Autowired
    private VoucherRepository voucherRepository;

    @Override
    public VoucherResponse getVoucherByVoucherId(Long voucherId) {
        return voucherResponse(getVoucherById(voucherId));
    }

    @Override
    public ListVoucherResponse listVoucherByShopId(Long shopId) {
        Date date = Date.from(new Date().toInstant());

        List<Voucher> vouchers = voucherRepository
                .findAllByShopShopIdAndStatusAndStartDateBeforeAndEndDateAfter(
                        shopId, Status.ACTIVE, date, date)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy mã giảm giá trên cửa hàng!"));
        return listVoucherResponse(vouchers, "Lấy danh sách mã giảm giá trong cửa hàng thành công.");
    }

    @Override
    public ListVoucherResponse listVoucherByType(VoucherType type) {
        List<Voucher> vouchers = voucherRepository.findAllByStatusAndType(Status.ACTIVE, type)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy mã giảm giá!"));

        return listVoucherResponse(vouchers, "Lấy danh sách mã giảm giá theo loại thành công.");
    }

    @Override
    public ListVoucherResponse listVoucherSystem() {
        Date date = Date.from(new Date().toInstant());
        List<Voucher> vouchers = voucherRepository.findAllByShopNullAndStatusAndStartDateBeforeAndEndDateAfter(
                Status.ACTIVE, date, date)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy mã giảm giá!"));

        return listVoucherResponse(vouchers, "Lấy danh sách mã giảm giá cửa hệ thống thành công.");
    }

    @Override
    public ListVoucherResponse allVoucher() {
        List<Voucher> vouchers = voucherRepository.findAllByStatus(Status.ACTIVE)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy mã giảm giá!"));

        return listVoucherResponse(vouchers, "Lấy danh sách tất cả khã dụng mã giảm giá thành công.");
    }

    @Override
    public VoucherResponse voucherResponse(Voucher voucher) {
        VoucherResponse response = new VoucherResponse();
        response.setVoucherDTO(VoucherDTO.convertEntityToDTO(voucher));
        response.setMessage("Lấy mã giảm giá thành công.");
        response.setStatus("ok");
        return response;
    }

    @Override
    public ListVoucherResponse listVoucherResponse(List<Voucher> vouchers, String message) {
        ListVoucherResponse response = new ListVoucherResponse();
        response.setVoucherDTOs(VoucherDTO.convertEntitiesToDTOs(vouchers));
        response.setMessage(message);
        response.setStatus("ok");
        response.setCount(vouchers.size());
        response.setCode(200);

        return response;
    }

    @Override
    public Voucher getVoucherById(Long voucherId) {
        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy mã giảm giá!"));
        if (voucher.getStatus().equals(Status.DELETED)) {
            throw new BadRequestException("Mã giảm giá đã bị xóa!");
        }
        return voucher;

    }

}
