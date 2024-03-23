package hcmute.kltn.vtv.controller.wallet;


import hcmute.kltn.vtv.model.data.wallet.response.LoyaltyPointResponse;
import hcmute.kltn.vtv.model.entity.wallet.LoyaltyPoint;
import hcmute.kltn.vtv.service.wallet.ILoyaltyPointService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer/loyalty-point")
@RequiredArgsConstructor
public class LoyaltyPointController {

    @Autowired
    private ILoyaltyPointService loyaltyPointService;


    @GetMapping("/get")
    public ResponseEntity<LoyaltyPointResponse> getLoyaltyPointByUsername(HttpServletRequest httpServletRequest) {
        String username = (String) httpServletRequest.getAttribute("username");
        return ResponseEntity.ok(loyaltyPointService.getLoyaltyPointResponseByUsername(username));
    }

}
