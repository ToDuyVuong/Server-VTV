package hcmute.kltn.vtv.service.user.impl;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.user.response.CustomerVoucherResponse;
import hcmute.kltn.vtv.model.data.user.response.ListCustomerVoucherResponse;
import hcmute.kltn.vtv.model.dto.vtv.VoucherDTO;
import hcmute.kltn.vtv.model.entity.user.CustomerVoucher;
import hcmute.kltn.vtv.model.entity.vtv.Voucher;
import hcmute.kltn.vtv.repository.user.CustomerVoucherRepository;
import hcmute.kltn.vtv.repository.vtv.VoucherRepository;
import hcmute.kltn.vtv.service.guest.IVoucherService;
import hcmute.kltn.vtv.service.user.ICustomerService;
import hcmute.kltn.vtv.service.user.ICustomerVoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerVoucherServiceImpl implements ICustomerVoucherService {

    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private CustomerVoucherRepository customerVoucherRepository;
    @Autowired
    private IVoucherService voucherService;
    @Autowired
    private ICustomerService customerService;

    @Override
    public CustomerVoucherResponse saveVoucher(Long voucherId, String username) {
        if (customerVoucherRepository.existsByCustomerUsernameAndVoucherVoucherId(username, voucherId)) {
            throw new BadRequestException("Bạn đã lưu mã giảm giá này rồi!");
        }

        CustomerVoucher customerVoucher = new CustomerVoucher();
        customerVoucher.setCustomer(customerService.getCustomerByUsername(username));
        customerVoucher.setVoucher(voucherService.getVoucherById(voucherId));
        customerVoucher.setUsed(false);

        try {
            customerVoucherRepository.save(customerVoucher);
            return CustomerVoucherResponse.customerVoucherResponse(customerVoucher.getVoucher(),
                    "Thêm mã giảm giá thành công!", username, "Success");
        } catch (Exception e) {
            throw new BadRequestException("Lưu mã giảm giá thất bại!");
        }

    }

    @Override
    public ListCustomerVoucherResponse listCustomerVoucherByUsername(String username) {
        List<Voucher> vouchers = voucherRepository.getAllByUsernameAndUsed(username, false)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy mã giảm giá!"));

        return ListCustomerVoucherResponse.listCustomerVoucherResponse(vouchers, "Lấy danh sách mã giảm giá thành công.",
                username, "OK");
    }

    @Override
    public CustomerVoucherResponse deleteCustomerVoucher(Long voucherId, String username) {
        if (!customerVoucherRepository.existsByCustomerUsernameAndVoucherVoucherId(username, voucherId)) {
            throw new BadRequestException("Bạn chưa lưu mã giảm giá này!");
        }

        CustomerVoucher customerVoucher = customerVoucherRepository
                .findByCustomerUsernameAndVoucherVoucherId(username, voucherId)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy mã giảm giá!"));

        if (customerVoucher.isUsed()) {
            throw new BadRequestException("Mã giảm giá đã được sử dụng!");
        }

        try {
            customerVoucherRepository.delete(customerVoucher);
            return CustomerVoucherResponse.customerVoucherResponse(customerVoucher.getVoucher(),
                    "Xóa mã giảm giá thành công!", username, "Success");
        } catch (Exception e) {
            throw new BadRequestException("Xóa mã giảm giá thất bại!");
        }
    }


}
