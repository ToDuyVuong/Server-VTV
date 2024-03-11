package hcmute.kltn.vtv.model.data.guest;

import hcmute.kltn.vtv.model.dto.vtv.BrandDTO;
import hcmute.kltn.vtv.model.entity.vtv.Brand;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BrandResponse extends ResponseAbstract {

    private BrandDTO brandDTO;


    public static BrandResponse brandResponse(Brand brand, String message, String status) {
        BrandResponse brandResponse = new BrandResponse();
        brandResponse.setBrandDTO(BrandDTO.convertEntityToDTO(brand));
        brandResponse.setMessage(message);
        brandResponse.setStatus(status);
        brandResponse.setCode(200);

        return brandResponse;
    }
}

