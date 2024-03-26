package hcmute.kltn.vtv.model.dto.user;

import hcmute.kltn.vtv.model.entity.user.VoucherOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class VoucherOrderDTO {

    private Long voucherOrderId;

    private Long voucherId;

    private String voucherName;

    private boolean type; // true: voucher system, false: voucher shop

    private UUID orderId;

    public static VoucherOrderDTO convertEntityToDTO(VoucherOrder voucherOrder) {
        VoucherOrderDTO voucherOrderDTO = new VoucherOrderDTO();
        if (voucherOrder.getVoucherOrderId() != null) {
            voucherOrderDTO.setVoucherOrderId(voucherOrder.getVoucherOrderId());
            voucherOrderDTO.setOrderId(voucherOrder.getOrder().getOrderId());
        }
        voucherOrderDTO.setVoucherId(voucherOrder.getVoucher().getVoucherId());
        voucherOrderDTO.setVoucherName(voucherOrder.getVoucher().getName());
        voucherOrderDTO.setType(voucherOrder.isType());
        return voucherOrderDTO;

    }

    public static List<VoucherOrderDTO> convertEntitiesToDTOs(List<VoucherOrder> voucherOrders) {
        if (voucherOrders == null) {
            return null;
        }

        List<VoucherOrderDTO> voucherOrderDTOs = new ArrayList<>();
        for (VoucherOrder voucherOrder : voucherOrders) {
            VoucherOrderDTO voucherOrderDTO = convertEntityToDTO(voucherOrder);
            voucherOrderDTOs.add(voucherOrderDTO);
        }
        return voucherOrderDTOs;
    }
}
