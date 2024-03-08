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
public class CommentRequest {

    private String content;

    private UUID reviewId;

    private String username;

    private boolean isShop;

    public void validate() {

        if (content == null || content.isEmpty()) {
            throw new BadRequestException("Nội dung không được để trống.");
        }

        if (reviewId == null) {
            throw new BadRequestException("Mã review không được để trống.");
        }

        this.content = this.content.trim();
    }

}
