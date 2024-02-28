package hcmute.kltn.vtv.model.data.location;

import hcmute.kltn.vtv.model.dto.location.DistrictDTO;
import hcmute.kltn.vtv.model.dto.location.ProvinceDTO;
import hcmute.kltn.vtv.model.dto.location.WardDTO;
import hcmute.kltn.vtv.model.entity.location.Ward;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LocationResponse extends ResponseAbstract {

    private String administrativeRegionName;

    private ProvinceDTO provinceDTO;

    private DistrictDTO districtDTO;

    private WardDTO wardDTO;

    public static LocationResponse convertWardToResponse(Ward ward) {
        LocationResponse locationResponse = new LocationResponse();
        locationResponse.setWardDTO(WardDTO.convertEntityToDTO(ward));
        locationResponse.setDistrictDTO(DistrictDTO.convertEntityToDTO(ward.getDistrict()));
        locationResponse.setProvinceDTO(ProvinceDTO.convertEntityToDTO(ward.getDistrict().getProvince()));
        locationResponse.setAdministrativeRegionName(ward.getDistrict().getProvince().getAdministrativeRegion().getName());
        return locationResponse;
    }


}
