package hcmute.kltn.vtv.model.data.user.request;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {

    private String content;

    private int rating;

    private UUID orderItemId;

    private MultipartFile image;

    private boolean hasImage;


    public void validate() {

        if (content == null || content.isEmpty()) {
            throw new BadRequestException("Nội dung không được để trống.");
        }

        if (rating < 1 || rating > 5) {
            throw new BadRequestException("Đánh giá không hợp lệ. Đánh giá từ 1 đến 5 sao.");
        }

        if (orderItemId == null) {
            throw new BadRequestException("Đơn hàng không được để trống.");
        }

        if (this.hasImage && (this.image == null || this.image.isEmpty())) {
            throw new BadRequestException("Không thể thêm hình ảnh khi không có hình ảnh mới!");
        }

        if (this.hasImage && !isImage(this.image)) {
            throw new BadRequestException("Hình ảnh không hợp lệ! Vui lòng chọn hình ảnh khác!");
        }

        this.content = this.content.trim();

    }


    private boolean isImage(MultipartFile file) {
        return file != null && file.getContentType() != null && file.getContentType().startsWith("image");
    }

}
