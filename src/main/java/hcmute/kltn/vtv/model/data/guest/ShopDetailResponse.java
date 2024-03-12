package hcmute.kltn.vtv.model.data.guest;

import hcmute.kltn.vtv.model.dto.vtv.CategoryDTO;
import hcmute.kltn.vtv.model.dto.vendor.ProductDTO;
import hcmute.kltn.vtv.model.dto.vtv.ShopDTO;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShopDetailResponse extends ResponseAbstract {

    ShopDTO shopDTO;

    int totalCategory;

    List<CategoryDTO> categoryDTOs;

    int totalProduct;

    List<ProductDTO> productDTOs;

    boolean isFollowed;
}
