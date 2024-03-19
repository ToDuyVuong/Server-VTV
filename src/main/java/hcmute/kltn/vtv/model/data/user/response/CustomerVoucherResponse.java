package hcmute.kltn.vtv.model.data.user.response;

import hcmute.kltn.vtv.model.dto.vtv.VoucherDTO;
import hcmute.kltn.vtv.model.entity.vendor.Voucher;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CustomerVoucherResponse extends ResponseAbstract {
    private String username;
    private VoucherDTO voucherDTO;

    public static CustomerVoucherResponse customerVoucherResponse(Voucher voucher, String message, String username, String status) {
        CustomerVoucherResponse response = new CustomerVoucherResponse();
        response.setVoucherDTO(VoucherDTO.convertEntityToDTO(voucher));
        response.setUsername(username);
        response.setMessage(message);
        response.setStatus(status);
        response.setCode(200);
        return response;
    }
}
