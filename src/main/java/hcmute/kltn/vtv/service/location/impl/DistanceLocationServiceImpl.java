package hcmute.kltn.vtv.service.location.impl;

import hcmute.kltn.vtv.model.entity.location.Ward;
import hcmute.kltn.vtv.model.extra.RegionLevel;
import hcmute.kltn.vtv.service.location.IDistanceLocationService;
import hcmute.kltn.vtv.service.location.IRegionMatcherService;
import hcmute.kltn.vtv.service.location.IWardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DistanceLocationServiceImpl implements IDistanceLocationService {

    private final IWardService wardService;
    private final IRegionMatcherService regionMatcherService;


    @Override
    public int calculateDistance(String wardCodeCustomer, String wardCodeShop) {
        Ward locationCustomer = wardService.getWardByWardCode(wardCodeCustomer);
        Ward locationShop = wardService.getWardByWardCode(wardCodeShop);

        return findMatchingLevel(locationCustomer, locationShop);
    }


    @Override
    public String messageByLeverRegion(int level) {
        switch (level) {
            case 0 -> {
                return "Cùng phường / xã";
            }
            case 1 -> {
                return "Cùng quận / huyện";
            }
            case 2 -> {
                return "Cùng tỉnh / thành phố";
            }
            case 3 -> {
                return "Cùng vùng miền";
            }
            case 4 -> {
                return "Cùng quốc gia";
            }
            default -> {
                return "Không cùng khu vực";
            }
        }
    }

    private int findMatchingLevel(Ward customer, Ward shop) {
        for (int i = 0; i < RegionLevel.values().length; i++) {
            if (regionMatcherService.matches(customer, shop, RegionLevel.values()[i])) {
                return  i;
            }
        }
        return  RegionLevel.values().length;
    }


}




