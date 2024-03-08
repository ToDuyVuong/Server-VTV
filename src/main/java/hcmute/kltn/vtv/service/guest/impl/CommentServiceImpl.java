package hcmute.kltn.vtv.service.guest.impl;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.guest.ListCommentResponse;
import hcmute.kltn.vtv.model.dto.user.CommentDTO;
import hcmute.kltn.vtv.model.entity.user.Comment;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.user.CommentRepository;
import hcmute.kltn.vtv.service.guest.ICommentService;
import hcmute.kltn.vtv.service.user.IReviewCustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements ICommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private final IReviewCustomerService reviewCustomerService;

    @Override
    public List<CommentDTO> getListCommentDTO(UUID reviewId) {
        reviewCustomerService.checkReview(reviewId);

        List<Comment> comments = commentRepository.findAllByReviewReviewIdAndStatus(reviewId, Status.ACTIVE)
                .orElseThrow(() -> new BadRequestException("Không có bình luận nào cho đánh giá này."));

        // System.out.println("CommentDTO.convertEntitiesToDTOs(comments) = " +
        // CommentDTO.convertEntitiesToDTOs(comments));

        return CommentDTO.convertEntitiesToDTOs(comments);
    }

    @Override
    public ListCommentResponse getComments(UUID reviewId) {
        reviewCustomerService.checkReview(reviewId);

        List<Comment> comments = commentRepository.findAllByReviewReviewIdAndStatus(reviewId, Status.ACTIVE)
                .orElseThrow(() -> new BadRequestException("Không có bình luận nào cho đánh giá này."));

        return listCommentResponse(comments, reviewId);
    }

    private ListCommentResponse listCommentResponse(List<Comment> comments, UUID reviewId) {
        ListCommentResponse response = new ListCommentResponse();
        response.setCommentDTOs(CommentDTO.convertEntitiesToDTOs(comments));

        System.out
                .println("CommentDTO.convertEntitiesToDTOs(comments) = " + CommentDTO.convertEntitiesToDTOs(comments));

        response.setCount(comments.size());
        System.out.println("comments.size() = " + comments.size());
        response.setReviewId(reviewId);
        System.out.println("reviewId = " + reviewId);
        response.setMessage("Lấy danh sách bình luận của đánh giá thành công.");
        response.setCode(200);
        response.setStatus("OK");
        return response;
    }

}
