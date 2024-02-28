package hcmute.kltn.vtv.service.location.impl;

import hcmute.kltn.vtv.model.entity.location.Ward;
import hcmute.kltn.vtv.model.extra.RegionLevel;
import hcmute.kltn.vtv.service.location.IRegionMatcherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RegionMatcherServiceImpl implements IRegionMatcherService {

    @Override
    public boolean matches(Ward customerWard, Ward shopWard, RegionLevel level) {
        String customerCode = getCodeByLevel(customerWard, level);
        String shopCode = getCodeByLevel(shopWard, level);
        return customerCode.equals(shopCode);
    }

    private String getCodeByLevel(Ward ward, RegionLevel level) {
        switch (level) {
            case WARD -> {
                return ward.getWardCode();
            }
            case DISTRICT -> {
                return ward.getDistrict().getDistrictCode();
            }
            case PROVINCE -> {
                return ward.getDistrict().getProvince().getProvinceCode();
            }
            case ADMINISTRATIVE_REGION -> {
                return ward.getDistrict().getProvince().getAdministrativeRegion().getCodeName();
            }
        }
        throw new IllegalArgumentException("Không hỗ trợ cấp độ: " + level);
    }



}
