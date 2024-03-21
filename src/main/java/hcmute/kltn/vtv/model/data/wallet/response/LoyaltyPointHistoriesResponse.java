package hcmute.kltn.vtv.model.data.wallet.response;


import hcmute.kltn.vtv.model.dto.wallet.LoyaltyPointHistoryDTO;
import hcmute.kltn.vtv.model.entity.wallet.LoyaltyPointHistory;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltyPointHistoriesResponse  extends ResponseAbstract {

    private int count;
    private List<LoyaltyPointHistoryDTO> loyaltyPointHistoryDTOs;

    public static LoyaltyPointHistoriesResponse loyaltyPointHistoriesResponse(List<LoyaltyPointHistory> loyaltyPointHistories, String message, String status) {
        LoyaltyPointHistoriesResponse response = new LoyaltyPointHistoriesResponse();
        response.setLoyaltyPointHistoryDTOs(LoyaltyPointHistoryDTO.convertEntitiesToDTOs(loyaltyPointHistories));
        response.setCount(loyaltyPointHistories.size());
        response.setMessage(message);
        response.setStatus(status);
        response.setCode(200);

        return response;
    }
}
