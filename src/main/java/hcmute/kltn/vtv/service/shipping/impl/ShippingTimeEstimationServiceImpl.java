package hcmute.kltn.vtv.service.shipping.impl;

import hcmute.kltn.vtv.service.shipping.IShippingTimeEstimationService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ShippingTimeEstimationServiceImpl implements IShippingTimeEstimationService {


    @Override
    public Date estimateDeliveryTime(int distance, String shippingProvider) {
        return switch (shippingProvider) {
            case "VTV Express" -> calculateDeliveryTime(distance, 1);
            case "GHN" -> calculateDeliveryTime(distance, 2);
            case "GHTK" -> calculateDeliveryTime(distance, 3);
            default -> null;
        };
    }

    private Date calculateDeliveryTime(int distance, int additionalDays) {
        if (distance < 0 || distance > 4) {
            throw new BadRequestException("Khoảng cách không hợp lệ");
        }

        LocalDate today = LocalDate.now();

        return Date.from(today.plusDays(distance + additionalDays).atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());
    }
}
