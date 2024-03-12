package hcmute.kltn.vtv.model.data.vtv.request;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Data
@ToString
@RequiredArgsConstructor
public class BrandRequest {

    private String name;

    private MultipartFile image;

    private boolean changeImage;

    private String description;

    private String information;

    private String origin;

    private boolean allCategories;

    private List<Long> categories;

    public void validate() {

        if (this.name == null || this.name.isEmpty()) {
            throw new BadRequestException("Tên thương hiệu không được để trống!");
        }

        if (this.description == null || this.description.isEmpty()) {
            throw new BadRequestException("Mô tả thương hiệu không được để trống!");
        }

        if (this.information == null || this.information.isEmpty()) {
            throw new BadRequestException("Thông tin thương hiệu không được để trống!");
        }

        if (this.origin == null || this.origin.isEmpty()) {
            throw new BadRequestException("Xuất xứ thương hiệu không được để trống!");
        }


        if (!this.allCategories && (this.categories == null || this.categories.isEmpty())) {
            throw new BadRequestException("Danh sách danh mục không được để trống!");
        }


        if (this.changeImage && (this.image == null || this.image.isEmpty())) {
            throw new BadRequestException("Không thể thay đổi hình ảnh khi không có hình ảnh mới!");
        }

        // check image
        if (this.changeImage && !isImage(this.image)) {
            throw new BadRequestException("Hình ảnh không hợp lệ! Vui lòng chọn hình ảnh khác!");
        }




        trim();

    }


    private boolean isImage(MultipartFile file) {
        return file != null && file.getContentType() != null && file.getContentType().startsWith("image");
    }


    public void trim() {
        this.name = this.name.trim();
        this.description = this.description.trim();
        this.information = this.information.trim();
        this.origin = this.origin.trim();
    }
}
