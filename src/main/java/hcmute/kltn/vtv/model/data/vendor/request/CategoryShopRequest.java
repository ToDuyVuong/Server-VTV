package hcmute.kltn.vtv.model.data.vendor.request;


import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@ToString
@RequiredArgsConstructor
public class CategoryShopRequest {


    private String name;

    private MultipartFile image;

    private boolean changeImage;

    private List<Long> productIds;


    public void validate() {
        if (this.name == null || this.name.isEmpty()) {
            throw new BadRequestException("Tên danh mục không được để trống!");
        }
        if (this.changeImage && (this.image == null || this.image.isEmpty())) {
            throw new BadRequestException("Không thể thay đổi hình ảnh khi không có hình ảnh mới!");
        }

        if (this.changeImage && !isImage(this.image)) {
            throw new BadRequestException("Hình ảnh không hợp lệ! Vui lòng chọn hình ảnh khác!");
        }
    }

    private boolean isImage(MultipartFile file) {
        return file != null && file.getContentType() != null && file.getContentType().startsWith("image");
    }

}
