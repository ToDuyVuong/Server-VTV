package hcmute.kltn.vtv.model.data.guest;

import hcmute.kltn.vtv.model.dto.vendor.ProductDTO;
import hcmute.kltn.vtv.model.entity.vendor.Product;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse extends ResponseAbstract {

    private Long categoryId;
    private String categoryName;
    private Long categoryParentId;
    private String categoryParentName;
    private Long shopId;
    private String shopName;
    private String shopAvatar;
    private int countOrder;
    private ProductDTO productDTO;


    public static ProductResponse productResponse(Product product, int countOrder, String message, String status) {
        ProductResponse response = new ProductResponse();
        response.setProductDTO(ProductDTO.convertEntityToDTO(product));
        response.setCategoryId(product.getCategory().getCategoryId());
        response.setCategoryName(product.getCategory().getName());
        response.setCategoryParentId(product.getCategory().getParent() != null ? product.getCategory().getParent().getCategoryId() : null);
        response.setCategoryParentName(product.getCategory().getParent() != null ? product.getCategory().getParent().getName() : null);
        response.setShopId(product.getShop().getShopId());
        response.setShopName(product.getShop().getName());
        response.setShopAvatar(product.getShop().getAvatar());
        response.setCountOrder(countOrder);
        response.setMessage(message);
        response.setStatus(status);
        response.setCode(200);
        return response;
    }




}
