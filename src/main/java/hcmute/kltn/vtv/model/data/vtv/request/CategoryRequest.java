package hcmute.kltn.vtv.model.data.vtv.request;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Data
@ToString
@RequiredArgsConstructor
public class CategoryRequest {

    private String name;

    private String description;

    private MultipartFile image;

    private boolean changeImage;

    private boolean child;

    private Long parentId;

    public void validate() {
        if (this.name == null || this.name.isEmpty()) {
            throw new BadRequestException("Tên danh mục không được để trống!");
        }

        if (this.description == null || this.description.isEmpty()) {
            throw new BadRequestException("Mô tả danh mục không được để trống!");
        }

        if (this.image == null || this.image.isEmpty()) {
            throw new BadRequestException("Hình ảnh danh mục không được để trống!");
        }

        if (this.child && this.parentId == null) {
            throw new BadRequestException("Danh mục cha không được để trống!");
        }


        if (this.changeImage && (this.image == null || this.image.isEmpty())) {
            throw new BadRequestException("Không thể thay đổi hình ảnh khi không có hình ảnh mới!");
        }

        if (this.changeImage && !isImage(this.image)) {
            throw new BadRequestException("Hình ảnh không hợp lệ! Vui lòng chọn hình ảnh khác!");
        }


        trim();
    }

    public void trim() {
        this.name = this.name.trim();
        this.description = this.description.trim();
    }

    private boolean isImage(MultipartFile file) {
        return file != null && file.getContentType() != null && file.getContentType().startsWith("image");
    }
}
