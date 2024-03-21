package hcmute.kltn.vtv.model.dto.wallet;

import hcmute.kltn.vtv.model.entity.wallet.LoyaltyPoint;
import hcmute.kltn.vtv.model.extra.Status;
import jakarta.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltyPointDTO {

    private Long loyaltyPointId;

    private Long totalPoint;

    private Status status;

    private String username;

    private LocalDateTime updateAt;


    public static LoyaltyPointDTO convertEntityToDTO(LoyaltyPoint loyaltyPoint) {
        LoyaltyPointDTO loyaltyPointDTO = new LoyaltyPointDTO();
        loyaltyPointDTO.setLoyaltyPointId(loyaltyPoint.getLoyaltyPointId());
        loyaltyPointDTO.setTotalPoint(loyaltyPoint.getTotalPoint());
        loyaltyPointDTO.setStatus(loyaltyPoint.getStatus());
        loyaltyPointDTO.setUsername(loyaltyPoint.getUsername());
        loyaltyPointDTO.setUpdateAt(loyaltyPoint.getUpdateAt());
        return loyaltyPointDTO;
    }


}
