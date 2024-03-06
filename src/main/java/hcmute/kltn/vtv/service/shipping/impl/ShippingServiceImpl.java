package hcmute.kltn.vtv.service.shipping.impl;

import hcmute.kltn.vtv.model.data.shipping.response.ListShippingResponse;
import hcmute.kltn.vtv.model.data.shipping.response.ShippingResponse;
import hcmute.kltn.vtv.model.dto.shipping.ShippingDTO;
import hcmute.kltn.vtv.model.entity.location.Province;
import hcmute.kltn.vtv.model.entity.shipping.TransportProvider;
import hcmute.kltn.vtv.service.location.IDistanceLocationService;
import hcmute.kltn.vtv.service.location.IProvinceService;
import hcmute.kltn.vtv.service.shipping.IShippingService;
import hcmute.kltn.vtv.service.shipping.IShippingTimeEstimationService;
import hcmute.kltn.vtv.service.shipping.ITransportProviderService;
import hcmute.kltn.vtv.service.vtv.shippingstrategy.GiaoHangNhanhShippingStrategy;
import hcmute.kltn.vtv.service.vtv.shippingstrategy.GiaoHangTietKiemShippingStrategy;
import hcmute.kltn.vtv.service.vtv.shippingstrategy.IShippingStrategy;
import hcmute.kltn.vtv.service.vtv.shippingstrategy.VtvExpressShippingStrategy;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private final IDistanceLocationService distanceLocationService;
    @Autowired
    private final IShippingTimeEstimationService deliveryTimeEstimationService;
    @Autowired
    private final ITransportProviderService transportProviderService;
    @Autowired
    private final IProvinceService provinceService;


    @Override
    public ShippingResponse getCalculateShippingByWardAndTransportProvider(String wardCodeCustomer, String wardCodeShop, String shippingProvider) {


        ShippingDTO shippingDTO = calculateShippingDTO(wardCodeCustomer, wardCodeShop, shippingProvider);

        return ShippingResponse.shippingResponse(shippingDTO, "Tính phí vận chuyển của " + shippingProvider + " thành công.", "OK");
    }


    @Override
    public ListShippingResponse getShippingProvidersByWard(String wardCodeCustomer, String wardCodeShop) {

        Province provinceCustomer = provinceService.getProvinceByWardCode(wardCodeCustomer);
        Province provinceShop = provinceService.getProvinceByWardCode(wardCodeShop);
        List<TransportProvider> transportProviders = transportProviderService
                .getTransportProvidersByProvince(provinceShop.getProvinceCode(), provinceCustomer.getProvinceCode());
        List<ShippingDTO> shippingDTOs = calculateShippingDTOs(transportProviders, wardCodeCustomer, wardCodeShop);
        return ListShippingResponse.listShippingResponse(shippingDTOs, "Lấy danh sách nhà vận chuyển và tính phí vận chuyển thành công.", "OK");
    }


    @Override
    public void checkShippingProvider(String shippingProvider) {
        if (!shippingProvider.equals("VTV Express") && !shippingProvider.equals("GHN") && !shippingProvider.equals("GHTK")) {
            throw new BadRequestException("Đơn vị vận chuyển không hợp lệ! Vui lòng chọn VTV Express, GHN hoặc GHTK.");
        }
    }


    private List<ShippingDTO> calculateShippingDTOs(List<TransportProvider> transportProviders, String wardCodeCustomer, String wardCodeShop) {
        List<ShippingDTO> shippingDTOs = new ArrayList<>();
        for (TransportProvider transportProvider : transportProviders) {
            ShippingDTO shippingDTO = calculateShippingDTO(wardCodeCustomer, wardCodeShop, transportProvider.getShortName());
            shippingDTOs.add(shippingDTO);
        }

        return shippingDTOs;
    }


    private ShippingDTO calculateShippingDTO(String wardCodeCustomer, String wardCodeShop, String shippingProvider) {

        int distance = distanceLocationService.calculateDistance(wardCodeCustomer, wardCodeShop);
        Date estimatedDeliveryTime = deliveryTimeEstimationService.estimateDeliveryTime(distance, shippingProvider);
        IShippingStrategy shippingStrategy = getShippingStrategy(shippingProvider);
        Long shippingCost = shippingStrategy.calculateShippingCost(distance);
        TransportProvider transportProvider = transportProviderService.getTransportProviderByShortName(shippingProvider);

        return ShippingDTO.createShippingDTO(transportProvider, shippingCost,  estimatedDeliveryTime, new Date());
    }


    private IShippingStrategy getShippingStrategy(String shippingProvider) {
        return switch (shippingProvider) {
            case "VTV Express" -> new VtvExpressShippingStrategy();
            case "GHN" -> new GiaoHangNhanhShippingStrategy();
            case "GHTK" -> new GiaoHangTietKiemShippingStrategy();
            default ->
                    throw new IllegalArgumentException("Đơn vị vận chuyển không hợp lệ! Vui lòng chọn VTV Express, GHN hoặc GHTK.");
        };
    }

}
