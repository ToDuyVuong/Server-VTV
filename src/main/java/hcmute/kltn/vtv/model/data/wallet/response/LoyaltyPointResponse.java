package hcmute.kltn.vtv.model.data.wallet.response;


import hcmute.kltn.vtv.model.dto.wallet.LoyaltyPointDTO;
import hcmute.kltn.vtv.model.entity.wallet.LoyaltyPoint;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltyPointResponse  extends ResponseAbstract {

    private LoyaltyPointDTO loyaltyPointDTO;


    public static LoyaltyPointResponse loyaltyPointResponse(LoyaltyPoint loyaltyPoint, String message, String status) {
        LoyaltyPointResponse loyaltyPointResponse = new LoyaltyPointResponse();
        loyaltyPointResponse.setLoyaltyPointDTO(LoyaltyPointDTO.convertEntityToDTO(loyaltyPoint));
        loyaltyPointResponse.setMessage(message);
        loyaltyPointResponse.setStatus(status);
        loyaltyPointResponse.setCode(200);
        return loyaltyPointResponse;
    }
}
