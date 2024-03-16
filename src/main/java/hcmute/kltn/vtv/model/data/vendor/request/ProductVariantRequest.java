package hcmute.kltn.vtv.model.data.vendor.request;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@ToString
@RequiredArgsConstructor
public class ProductVariantRequest {

    private Long productVariantId;

    private String sku;

    private MultipartFile image;

    private boolean changeImage;

    private Long originalPrice;

    private Long price;

    private int quantity;

    private List<ProductAttributeRequest> productAttributeRequests;

    public void validate() {
        if (sku == null || sku.isEmpty()) {
            throw new BadRequestException("Mã biến thể sản phẩm không được để trống!");
        }

        this.sku = this.sku.trim();

        if (originalPrice == null) {
            throw new BadRequestException("Giá gốc sản phẩm không được để trống!");
        }

        if (originalPrice < 0) {
            throw new BadRequestException("Giá gốc sản phẩm không được nhỏ hơn 0!");
        }

        if (originalPrice < price) {
            throw new BadRequestException("Giá gốc sản phẩm không được nhỏ hơn giá bán!");
        }

        if (price < 0) {
            throw new BadRequestException("Giá sản phẩm không được nhỏ hơn 0!");
        }

        if (quantity == 0) {
            throw new BadRequestException("Số lượng sản phẩm không được để trống!");
        }

        if (quantity < 0) {
            throw new BadRequestException("Số lượng sản phẩm không được nhỏ hơn 0!");
        }

        if (changeImage && image == null) {
            throw new BadRequestException("Hình ảnh biến thể sản phẩm không được để trống!");
        }

        // check image
        if (changeImage && !isImage(image)) {
            throw new BadRequestException("Hình ảnh không hợp lệ! Vui lòng chọn hình ảnh khác!");
        }

        validateProductAttributeRequest(productAttributeRequests);
    }




    private boolean isImage(MultipartFile file) {
        return file != null && file.getContentType() != null && file.getContentType().startsWith("image");
    }



    private void validateProductAttributeRequest(List<ProductAttributeRequest> productAttributeRequest) {
        if (productAttributeRequest != null) {
            for (ProductAttributeRequest attributeRequest : productAttributeRequest) {
                attributeRequest.validate();
            }
            if (containsDuplicateByNameAndValue(productAttributeRequest)) {
                throw new BadRequestException("Có tên và giá trị thuộc tính sản phẩm trùng lặp trong danh sách thuộc tính của biến thể sản phẩm!");
            }
        }
    }


    private static boolean containsDuplicateByNameAndValue(List<ProductAttributeRequest> list) {
        List<String> attributeValues = new ArrayList<>();
        for (ProductAttributeRequest request : list) {
            String key = request.getName() + "_" + request.getValue();
            if (attributeValues.contains(key)) {
                return true;
            } else {
                attributeValues.add(key);
            }
        }
        return false;
    }


}