package hcmute.kltn.vtv.model.data.user.response;

import hcmute.kltn.vtv.model.dto.vtv.VoucherDTO;
import hcmute.kltn.vtv.model.entity.vendor.Voucher;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ListCustomerVoucherResponse extends ResponseAbstract {
    private String username;
    private int count;
    private List<VoucherDTO> voucherDTOs;


    public static ListCustomerVoucherResponse listCustomerVoucherResponse(List<Voucher> vouchers, String message,
                                                                          String username, String status) {
        ListCustomerVoucherResponse response = new ListCustomerVoucherResponse();
        response.setVoucherDTOs(VoucherDTO.convertToListDTO(vouchers));
        response.setUsername(username);
        response.setMessage(message);
        response.setStatus(status);
        response.setCount(vouchers.size());
        response.setCode(200);

        return response;
    }
}