package hcmute.kltn.vtv.service.user;

import hcmute.kltn.vtv.model.data.user.request.CommentRequest;
import hcmute.kltn.vtv.model.data.user.response.CommentResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface ICommentCustomerService {
    CommentResponse addNewComment(CommentRequest request);

    @Transactional
    CommentResponse deleteComment(UUID commentId, String username);

}
