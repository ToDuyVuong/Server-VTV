package hcmute.kltn.vtv.model.dto.wallet;

import hcmute.kltn.vtv.model.entity.wallet.LoyaltyPointHistory;
import hcmute.kltn.vtv.model.extra.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltyPointHistoryDTO {

    private Long loyaltyPointHistoryId;

    private Long point;

    private String type;

    private Status status;

    private Long loyaltyPointId;

    private LocalDateTime createAt;


    public static LoyaltyPointHistoryDTO convertEntityToDTO(LoyaltyPointHistory loyaltyPointHistory) {
        if (loyaltyPointHistory == null) {
            return null;
        }
        LoyaltyPointHistoryDTO loyaltyPointHistoryDTO = new LoyaltyPointHistoryDTO();
        loyaltyPointHistoryDTO.setLoyaltyPointHistoryId(loyaltyPointHistory.getLoyaltyPointHistoryId());
        loyaltyPointHistoryDTO.setPoint(loyaltyPointHistory.getPoint());
        loyaltyPointHistoryDTO.setType(loyaltyPointHistory.getType());
        loyaltyPointHistoryDTO.setStatus(loyaltyPointHistory.getStatus());
        loyaltyPointHistoryDTO.setCreateAt(loyaltyPointHistory.getCreateAt());
        loyaltyPointHistoryDTO.setLoyaltyPointId(loyaltyPointHistory.getLoyaltyPoint().getLoyaltyPointId());

        return loyaltyPointHistoryDTO;
    }


    public static List<LoyaltyPointHistoryDTO> convertEntitiesToDTOs(List<LoyaltyPointHistory> loyaltyPointHistories) {

        // sort by createAt desc
        return loyaltyPointHistories.stream().map(LoyaltyPointHistoryDTO::convertEntityToDTO)
                .sorted((o1, o2) -> o2.getCreateAt().compareTo(o1.getCreateAt()))
                .toList();
    }
}
