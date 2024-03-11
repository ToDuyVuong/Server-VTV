package hcmute.kltn.vtv.model.data.guest;

import hcmute.kltn.vtv.model.dto.vtv.BrandDTO;
import hcmute.kltn.vtv.model.entity.vtv.Brand;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ListBrandResponse extends ResponseAbstract {

    private int count;
    private List<BrandDTO> brandDTOs;


    public static ListBrandResponse listBrandResponse(List<Brand> brands, String message, String status) {
        ListBrandResponse listBrandResponse = new ListBrandResponse();
        listBrandResponse.setCount(brands.size());
        listBrandResponse.setBrandDTOs(BrandDTO.convertEntitiesToDTOs(brands));
        listBrandResponse.setMessage(message);
        listBrandResponse.setStatus(status);
        listBrandResponse.setCode(200);

        return listBrandResponse;
    }
}
