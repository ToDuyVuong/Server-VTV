package hcmute.kltn.vtv.model.data.wallet.response;


import hcmute.kltn.vtv.model.dto.wallet.LoyaltyPointHistoryDTO;
import hcmute.kltn.vtv.model.entity.wallet.LoyaltyPointHistory;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltyPointHistoryResponse  extends ResponseAbstract {


    private LoyaltyPointHistoryDTO loyaltyPointHistoryDTO;

    public static LoyaltyPointHistoryResponse loyaltyPointHistoryResponse(LoyaltyPointHistory loyaltyPointHistory, String message, String status) {
        LoyaltyPointHistoryResponse response = new LoyaltyPointHistoryResponse();
        response.setLoyaltyPointHistoryDTO(LoyaltyPointHistoryDTO.convertEntityToDTO(loyaltyPointHistory));
        response.setMessage(message);
        response.setStatus(status);
        response.setCode(200);

        return response;
    }
}
