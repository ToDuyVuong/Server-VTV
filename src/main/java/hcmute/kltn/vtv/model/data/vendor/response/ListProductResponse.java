package hcmute.kltn.vtv.model.data.vendor.response;

import hcmute.kltn.vtv.model.dto.vendor.ProductDTO;
import hcmute.kltn.vtv.model.entity.vendor.Product;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ListProductResponse extends ResponseAbstract {

    private int count;

    private List<ProductDTO> productDTOs;

    public static ListProductResponse listProductResponse(List<Product> products, String message, String status) {
        ListProductResponse response = new ListProductResponse();
        response.setProductDTOs(ProductDTO.convertEntitiesToDTOs(products));
        response.setCode(200);
        response.setMessage(message);
        response.setStatus(status);
        return response;
    }
}
