package hcmute.kltn.vtv.controller.vendor;

import hcmute.kltn.vtv.model.data.vendor.request.ShopRequest;
import hcmute.kltn.vtv.model.data.vendor.request.UpdateShopRequest;
import hcmute.kltn.vtv.model.data.vendor.response.ShopResponse;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.service.vendor.IShopService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendor")
@RequiredArgsConstructor
@Validated
public class ShopController {

    @Autowired
    private final IShopService shopService;

    @PostMapping("/register")
    public ResponseEntity<ShopResponse> registerShop(@ModelAttribute ShopRequest request,
            HttpServletRequest httpServletRequest) {
        String username = (String) httpServletRequest.getAttribute("username");
        request.validate();

        return ResponseEntity.ok(shopService.registerShop(request, username));
    }

    @GetMapping("/shop/profile")
    public ResponseEntity<ShopResponse> getProfileShop(HttpServletRequest httpServletRequest) {
        String username = (String) httpServletRequest.getAttribute("username");

        return ResponseEntity.ok(shopService.getProfileShop(username));
    }

    @PutMapping("/shop/update")
    public ResponseEntity<ShopResponse> updateShop(@ModelAttribute ShopRequest request,
            HttpServletRequest httpServletRequest) {
        String username = (String) httpServletRequest.getAttribute("username");
        request.validate();

        return ResponseEntity.ok(shopService.updateShop(request, username));
    }

    @PatchMapping("/shop/update/status/{status}")
    public ResponseEntity<ShopResponse> updateStatusShop(@PathVariable Status status,
            HttpServletRequest httpServletRequest) {
        String username = (String) httpServletRequest.getAttribute("username");
        if (!status.equals(Status.ACTIVE) && !status.equals(Status.INACTIVE) && !status.equals(Status.DELETED)) {
            throw new IllegalArgumentException("Trạng thái không hợp lệ!, Vui lòng chọn trạng thái khác như: ACTIVE, INACTIVE, DELETED");
        }

        return ResponseEntity.ok(shopService.updateStatusShop(username, status));
    }
}
