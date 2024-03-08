package hcmute.kltn.vtv.service.guest;

import hcmute.kltn.vtv.model.data.guest.ListCommentResponse;
import hcmute.kltn.vtv.model.dto.user.CommentDTO;

import java.util.List;
import java.util.UUID;

public interface ICommentService {

    List<CommentDTO> getListCommentDTO(UUID reviewId);

    ListCommentResponse getComments(UUID reviewId);
}
