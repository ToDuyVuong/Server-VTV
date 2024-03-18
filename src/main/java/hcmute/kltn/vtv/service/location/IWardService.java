package hcmute.kltn.vtv.service.location;

import hcmute.kltn.vtv.model.data.location.ListWardResponse;
import hcmute.kltn.vtv.model.data.location.LocationResponse;
import hcmute.kltn.vtv.model.entity.location.Ward;

import java.util.List;

public interface IWardService {
    String getWardNameByWardCode(String wardCode);

    Ward getWardByWardCode(String wardCode);

    Ward checkWardCodeMatchWithFullLocation(String provinceName, String districtName, String wardName, String wardCode);

    ListWardResponse getAllWardByDistrictCode(String districtCode);


    List<Ward> getWardsByWardsCodeWithDistrictCode(List<String> wardsCode, String districtCode);

    LocationResponse getLocationByWardCode(String wardCode);

    void checkWardCodeExist(String wardCode);
}
