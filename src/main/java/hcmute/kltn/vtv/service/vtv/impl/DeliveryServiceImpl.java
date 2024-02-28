package hcmute.kltn.vtv.service.vtv.impl;

import hcmute.kltn.vtv.model.data.shipping.response.ShippingResponse;
import hcmute.kltn.vtv.service.location.IDistanceLocationService;
import hcmute.kltn.vtv.service.vtv.IDeliveryService;
import hcmute.kltn.vtv.service.vtv.IDeliveryTimeEstimationService;
import hcmute.kltn.vtv.service.vtv.shippingstrategy.GiaoHangNhanhShipping;
import hcmute.kltn.vtv.service.vtv.shippingstrategy.GiaoHangTietKiemShipping;
import hcmute.kltn.vtv.service.vtv.shippingstrategy.IShipping;
import hcmute.kltn.vtv.service.vtv.shippingstrategy.VtvExpressShipping;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements IDeliveryService {

    @Autowired
    private final IDistanceLocationService distanceLocationService;
    @Autowired
    private final IDeliveryTimeEstimationService deliveryTimeEstimationService;


    @Override
    public ShippingResponse calculateShipping(String wardCodeCustomer, String wardCodeShop, String shippingProvider) {
        int distance = distanceLocationService.calculateDistance(wardCodeCustomer, wardCodeShop);
        Date estimatedDeliveryTime = deliveryTimeEstimationService.estimateDeliveryTime(distance, shippingProvider);
        IShipping shippingStrategy = getShippingStrategy(shippingProvider);
        Long shippingCost = shippingStrategy.calculateShippingCost(distance);
        return shippingResponse(distance, shippingCost, estimatedDeliveryTime, shippingProvider);
    }


    public void checkShippingProvider(String shippingProvider) {
        if (!shippingProvider.equals("VTV Express") && !shippingProvider.equals("GHN") && !shippingProvider.equals("GHTK")) {
            throw new BadRequestException("Đơn vị vận chuyển không hợp lệ! Vui lòng chọn VTV Express, GHN hoặc GHTK.");
        }
    }


    private ShippingResponse shippingResponse(int distance, Long shippingCost, Date estimatedDeliveryTime, String shippingProvider) {
        ShippingResponse shippingResponse = new ShippingResponse();
        shippingResponse.setDistance(distance);
        shippingResponse.setShippingCost(shippingCost);
        shippingResponse.setCurrency("VNĐ");
        shippingResponse.setEstimatedDeliveryTime(estimatedDeliveryTime);
        shippingResponse.setShippingProvider(shippingProvider);
        shippingResponse.setStatus("OK");
        shippingResponse.setCode(200);
        shippingResponse.setMessage("Tính phí vận chuyển thành công.");
        shippingResponse.setTimestamp(new Date());

        return shippingResponse;
    }


    private IShipping getShippingStrategy(String shippingProvider) {
        return switch (shippingProvider) {
            case "VTV Express" -> new VtvExpressShipping();
            case "GHN" -> new GiaoHangNhanhShipping();
            case "GHTK" -> new GiaoHangTietKiemShipping();
            default -> throw new IllegalArgumentException("Đơn vị vận chuyển không hợp lệ!");
        };
    }

}
