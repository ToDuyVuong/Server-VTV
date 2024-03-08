package hcmute.kltn.vtv.model.data.user.response;

import hcmute.kltn.vtv.model.dto.user.CommentDTO;
import hcmute.kltn.vtv.model.entity.user.Comment;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse extends ResponseAbstract {

    private String username;
    private UUID reviewId;
    private CommentDTO commentDTO;

    public static CommentResponse commentResponse(Comment comment, UUID reviewId, String username, String message, String status) {
        CommentResponse response = new CommentResponse();
        response.setCommentDTO(CommentDTO.convertEntityToDTO(comment));
        response.setMessage(message);
        response.setCode(200);
        response.setUsername(username);
        response.setReviewId(reviewId);
        response.setStatus(status);

        return response;
    }
}
