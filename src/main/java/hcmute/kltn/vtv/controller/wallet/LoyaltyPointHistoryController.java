package hcmute.kltn.vtv.controller.wallet;


import hcmute.kltn.vtv.model.data.wallet.response.LoyaltyPointHistoriesResponse;
import hcmute.kltn.vtv.model.data.wallet.response.LoyaltyPointHistoryResponse;
import hcmute.kltn.vtv.service.wallet.ILoyaltyPointHistoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer/loyalty-point-history")
@RequiredArgsConstructor
public class LoyaltyPointHistoryController {

    private final ILoyaltyPointHistoryService loyaltyPointHistoryService;


    @GetMapping("/get-list/{loyaltyPointId}")
    public ResponseEntity<LoyaltyPointHistoriesResponse> getLoyaltyPointHistoryByLoyaltyPointId(HttpServletRequest httpServletRequest,
                                                                                                @PathVariable Long loyaltyPointId) {
        String username = (String) httpServletRequest.getAttribute("username");
        return ResponseEntity.ok(loyaltyPointHistoryService.getLoyaltyPointHistoriesResponseByLoyaltyPointId(loyaltyPointId, username));
    }
}
