package hcmute.kltn.vtv.model.data.location;

import hcmute.kltn.vtv.model.dto.location.WardDTO;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ListWardResponse extends ResponseAbstract {

    private int count;
    private String districtCode;
    List<WardDTO> wardDTOs;

    public static ListWardResponse listWardResponse(List<WardDTO> wardDTOs, String districtCode,
                                                    String message, String status) {
        ListWardResponse response = new ListWardResponse();
        response.setWardDTOs(wardDTOs);
        response.setCount(response.getWardDTOs().size());
        response.setDistrictCode(districtCode);
        response.setCode(200);
        response.setMessage("Lấy danh sách phường xã thành công.");
        response.setStatus("OK");
        return response;

    }

}