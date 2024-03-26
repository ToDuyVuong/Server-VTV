package hcmute.kltn.vtv.model.dto.vtv;

import hcmute.kltn.vtv.model.entity.vendor.Voucher;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.model.extra.VoucherType;
import lombok.*;

import java.util.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VoucherDTO {

    private Long voucherId;

    private Status status;

    private String code;

    private String name;

    private String description;

    private int discount;

    private int quantity;

    private Date startDate;

    private Date endDate;

    private int quantityUsed;

    private String type;

    public static VoucherDTO convertEntityToDTO(Voucher voucher) {
        VoucherDTO voucherDTO = new VoucherDTO();
        voucherDTO.setVoucherId(voucher.getVoucherId());
        voucherDTO.setStatus(voucher.getStatus());
        voucherDTO.setCode(voucher.getCode());
        voucherDTO.setName(voucher.getName());
        voucherDTO.setDescription(voucher.getDescription());
        voucherDTO.setDiscount(voucher.getDiscount());
        voucherDTO.setQuantity(voucher.getQuantity());
        voucherDTO.setStartDate(voucher.getStartDate());
        voucherDTO.setEndDate(voucher.getEndDate());
        voucherDTO.setQuantityUsed(voucher.getQuantityUsed());

        if (voucher.getType() == null ||
                voucher.getType().equals(VoucherType.PERCENTAGE_SHOP) ||
                voucher.getType().equals(VoucherType.PERCENTAGE_SYSTEM)) {
            voucherDTO.setType("Giảm theo phần trăm");
        }
        if (voucher.getType().equals(VoucherType.MONEY_SHOP) ||
                voucher.getType().equals(VoucherType.MONEY_SYSTEM)) {
            voucherDTO.setType("Giảm theo tiền");
        }
        if (voucher.getType().equals(VoucherType.SHIPPING)) {
            voucherDTO.setType("Giảm phí vận chuyển");
        }


        return voucherDTO;
    }

    public static List<VoucherDTO> convertEntitiesToDTOs(List<Voucher> vouchers) {
        List<VoucherDTO> voucherDTOs = new ArrayList<>();
        for (Voucher voucher : vouchers) {
            voucherDTOs.add(convertEntityToDTO(voucher));
        }

        voucherDTOs.sort(Comparator
                .comparing(VoucherDTO::getType)
                .thenComparing(VoucherDTO::getStartDate)
                .thenComparing(VoucherDTO::getEndDate)
                .reversed());

        return voucherDTOs;
    }

    public String generateRandomCode(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }
        return code.toString();
    }

}
