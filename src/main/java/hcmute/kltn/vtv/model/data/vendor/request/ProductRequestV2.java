package hcmute.kltn.vtv.model.data.vendor.request;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@ToString
@RequiredArgsConstructor
public class ProductRequestV2 {

    private Long productId;

    private String name;

    private String image;

    private String description;

    private String information;

    private Long categoryId;

    private Long brandId;

    private String username;

    private List<ProductVariantRequestV2> productVariantRequests;

    public void validate() {
        validateNotNullOrEmpty(name, "Tên sản phẩm không được để trống!");
        validateNotNullOrEmpty(image, "Hình ảnh sản phẩm không được để trống!");
        validateNotNullOrEmpty(description, "Mô tả sản phẩm không được để trống!");
        validateNotNullOrEmpty(information, "Thông tin sản phẩm không được để trống!");

        if (categoryId == null) {
            throw new BadRequestException("Danh mục sản phẩm không được để trống!");
        }

        Set<String> skuSet = productVariantRequests
                .stream()
                .map(ProductVariantRequestV2::getSku)
                .collect(Collectors.toSet());
        if (skuSet.size() < productVariantRequests.size()) {
            throw new BadRequestException("Có mã biến thể sản phẩm trùng lặp trong danh sách biến thể của sản phẩm!");
        }

        if (productVariantRequests == null || productVariantRequests.isEmpty()) {
            throw new BadRequestException("Sản phẩm phải có ít nhất 1 biến thể!");
        } else {
            for (ProductVariantRequestV2 variantRequest : productVariantRequests) {
                variantRequest.validate();
            }
        }

        trim();
    }

    public void validateUpdate() {
        if (productId == null) {
            throw new BadRequestException("Mã sản phẩm không được để trống!");
        }

        validate();

        for (ProductVariantRequestV2 variantRequest : productVariantRequests) {
            variantRequest.validateUpdate();
        }
    }

    private void validateNotNullOrEmpty(String field, String errorMessage) {
        if (field == null || field.isEmpty()) {
            throw new BadRequestException(errorMessage);
        }
    }

    public void trim() {
        this.name = this.name.trim();
        this.image = this.image.trim();
        this.description = this.description.trim();
        this.information = this.information.trim();
        this.username = this.username.trim();
    }

}
