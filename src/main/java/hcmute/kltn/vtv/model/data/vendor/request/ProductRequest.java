package hcmute.kltn.vtv.model.data.vendor.request;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@ToString
@RequiredArgsConstructor
public class ProductRequest {

    private Long productId;

    private String name;

    private MultipartFile image;

    private boolean changeImage;

    private String description;

    private String information;

    private Long categoryId;

    private Long brandId;

    private List<ProductVariantRequest> productVariantRequests;

    public void validate() {
        validateNotNullOrEmpty(name, "Tên sản phẩm không được để trống!");
        validateNotNullOrEmpty(description, "Mô tả sản phẩm không được để trống!");
        validateNotNullOrEmpty(information, "Thông tin sản phẩm không được để trống!");

        if (changeImage && image == null) {
            throw new BadRequestException("Hình ảnh sản phẩm không được để trống!");
        }

        // check image
        if (this.changeImage && !isImage(this.image)) {
            throw new BadRequestException("Hình ảnh không hợp lệ! Vui lòng chọn hình ảnh khác!");
        }


        if (categoryId == null) {
            throw new BadRequestException("Danh mục sản phẩm không được để trống!");
        }

        validateProductVariant(productVariantRequests);

        trim();
    }

    private boolean isImage(MultipartFile file) {
        return file != null && file.getContentType() != null && file.getContentType().startsWith("image");
    }


    private void validateProductVariant(List<ProductVariantRequest> productVariantRequests) {
        if (productVariantRequests.isEmpty()) {
            throw new BadRequestException("Sản phẩm phải có ít nhất 1 biến thể!");
        } else {
            for (ProductVariantRequest variantRequest : productVariantRequests) {
                variantRequest.validate();
            }
        }

        Set<String> skuSet = productVariantRequests.stream().map(ProductVariantRequest::getSku).collect(Collectors.toSet());
        if (skuSet.size() < productVariantRequests.size()) {
            throw new BadRequestException("Có mã biến thể sản phẩm trùng lặp trong danh sách biến thể của sản phẩm!");
        }
    }


    private void validateNotNullOrEmpty(String field, String errorMessage) {
        if (field == null || field.isEmpty()) {
            throw new BadRequestException(errorMessage);
        }
    }

    public void trim() {
        this.name = this.name.trim();
        this.description = this.description.trim();
        this.information = this.information.trim();
    }

}
