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

    private Long categoryParentId;
    private String categoryParentName;
    private Long categoryShopId;
    private String categoryShopName;
    private String shopName;
    private ProductDTO productDTO;


    public static ProductResponse productResponse(Product product, String message, String status) {
        ProductResponse response = new ProductResponse();
        response.setProductDTO(ProductDTO.convertEntityToDTO(product));
        response.setCategoryParentId(product.getCategory().getParent().getCategoryId());
        response.setCategoryParentName(product.getCategory().getParent().getName());
        response.setCategoryShopId(product.getCategory().getCategoryId());
        response.setCategoryShopName(product.getCategory().getName());
        response.setShopName(product.getShop().getName());
        response.setMessage(message);
        response.setStatus(status);
        response.setCode(200);
        return response;
    }

}
