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



    public static LocationResponse locationResponse(Ward ward, String message, String status) {
        LocationResponse response = new LocationResponse();
        response.setWardDTO(WardDTO.convertEntityToDTO(ward));
        response.setDistrictDTO(DistrictDTO.convertEntityToDTO(ward.getDistrict()));
        response.setProvinceDTO(ProvinceDTO.convertEntityToDTO(ward.getDistrict().getProvince()));
        response.setAdministrativeRegionName(ward.getDistrict().getProvince().getAdministrativeRegion().getName());
        response.setCode(200);
        response.setMessage(message);
        response.setStatus(status);

        return response;
    }


}
