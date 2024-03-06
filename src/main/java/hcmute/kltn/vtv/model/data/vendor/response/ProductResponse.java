package hcmute.kltn.vtv.model.data.vendor.response;

import hcmute.kltn.vtv.model.dto.vtv.ProductDTO;
import hcmute.kltn.vtv.model.entity.vtv.Product;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse extends ResponseAbstract {

    private String categoryName;
    private String shopName;
    private float rating;
    private ProductDTO productDTO;


    public static ProductResponse productResponse(Product product, String message, String status) {
        ProductResponse response = new ProductResponse();
        response.setProductDTO(ProductDTO.convertEntityToDTO(product));
        response.setMessage(message);
        response.setStatus(status);
        response.setCode(200);
        return response;
    }

}
