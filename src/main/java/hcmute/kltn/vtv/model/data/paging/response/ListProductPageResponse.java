package hcmute.kltn.vtv.model.data.paging.response;

import hcmute.kltn.vtv.model.dto.vendor.ProductDTO;
import hcmute.kltn.vtv.model.entity.vendor.Product;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ListProductPageResponse extends ResponseAbstract {

    private int count;
    private int page;
    private int size;
    private int totalPage;
    private List<ProductDTO> productDTOs;


    public static ListProductPageResponse listProductPageResponse(Page<Product> products,
                                                                  int size, String message) {
        ListProductPageResponse response = new ListProductPageResponse();
        response.setProductDTOs(ProductDTO.convertEntitiesToDTOs(products.getContent()));
        response.setCount(products.getNumberOfElements());
        response.setSize(products.getSize());
        response.setPage(products.getNumber() + 1);
        response.setTotalPage(products.getTotalPages());
        response.setMessage(message);
        response.setStatus("OK");
        response.setCode(200);
        return response;
    }
}
