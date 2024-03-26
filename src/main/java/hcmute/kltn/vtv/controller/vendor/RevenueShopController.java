package hcmute.kltn.vtv.controller.vendor;

import hcmute.kltn.vtv.model.data.vendor.response.StatisticsResponse;
import hcmute.kltn.vtv.service.vendor.IRevenueService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/vendor/shop/revenue")
@RequiredArgsConstructor
public class RevenueShopController {

    private final IRevenueService revenueService;

    @GetMapping("/statistics")
    public ResponseEntity<StatisticsResponse> statisticsByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
                                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
                                                               HttpServletRequest httpServletRequest) {
        String username = (String) httpServletRequest.getAttribute("username");
        // khoảng thời gian trong vòng 31
        if (endDate.getTime() - startDate.getTime() > 2678400000L) {
            throw new BadRequestException("Khoảng thời gian không được lớn hơn 31 ngày.");
        }
        return ResponseEntity.ok(revenueService.statisticsByDate(startDate, endDate, username));
    }

}
