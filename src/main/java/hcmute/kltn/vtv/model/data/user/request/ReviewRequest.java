package hcmute.kltn.vtv.model.data.user.request;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.*;

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

    private String image;

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

        this.content = this.content.trim();

    }

}
