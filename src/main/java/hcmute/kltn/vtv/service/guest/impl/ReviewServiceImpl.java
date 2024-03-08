package hcmute.kltn.vtv.service.guest.impl;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.guest.ListReviewResponse;
import hcmute.kltn.vtv.model.data.user.response.ReviewResponse;
import hcmute.kltn.vtv.model.dto.user.ReviewDTO;
import hcmute.kltn.vtv.model.entity.user.Review;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.user.ReviewRepository;
import hcmute.kltn.vtv.service.guest.IReviewService;
import hcmute.kltn.vtv.service.user.IReviewCustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements IReviewService {

    @Autowired
    private final ReviewRepository reviewRepository;
    @Autowired
    private final IReviewCustomerService reviewCustomerService;

    @Override
    public ReviewResponse getReviewDetailById(UUID reviewId) {
        Review review = reviewCustomerService.checkReview(reviewId);
        // List<CommentDTO> commentDTOs = commentService.getListCommentDTO(reviewId);
        //
        // ReviewDTO reviewDTO = ReviewDTO.convertEntityToDTO(review);
        // reviewDTO.setCommentDTOs(commentDTOs);

        ReviewResponse reviewResponse = new ReviewResponse();
        reviewResponse.setReviewDTO(ReviewDTO.convertEntityToDTO(review));
        reviewResponse.setProductId(review.getProduct().getProductId());
        reviewResponse.setMessage("Lấy thông tin đánh giá thành công.");
        reviewResponse.setStatus("OK");
        reviewResponse.setCode(200);

        return reviewResponse;
    }

    @Override
    public ListReviewResponse getReviewsByProductId(Long productId) {

        List<Review> reviews = reviewRepository.findAllByProductProductIdAndStatus(productId, Status.ACTIVE)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy đánh giá nào!"));

        return listReviewResponse(reviews, "Lấy danh sách đánh giá thành công!", productId);
    }

    @Override
    public ListReviewResponse getReviewsByProductIdAndRating(Long productId, int rating) {

        List<Review> reviews = reviewRepository
                .findAllByProductProductIdAndRatingAndStatus(productId, rating, Status.ACTIVE)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy đánh giá nào!"));

        return listReviewResponse(reviews, "Lấy danh sách đánh giá theo xếp hạng thành công!", productId);
    }

    @Override
    public ListReviewResponse getReviewsByProductIdAndImageNotNull(Long productId) {

        List<Review> reviews = reviewRepository.findAllByProductProductIdAndImageNotNull(productId)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy đánh giá nào!"));

        return listReviewResponse(reviews, "Lấy danh sách đánh giá có hình ảnh thành công!", productId);
    }

    @Override
    public int countReviewByProductId(Long productId) {
        return reviewRepository.countByProductProductId(productId);
    }

    @Override
    public float countAverageRatingByProductId(Long productId) {

        List<Review> reviews = reviewRepository.findAllByProductProductIdAndImageNotNull(productId)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy đánh giá nào!"));

        return averageRating(reviews);
    }

    private long averageRating(List<Review> reviews) {
        long sum = 0;
        for (Review review : reviews) {
            sum += review.getRating();
        }
        return !reviews.isEmpty() ? sum / reviews.size() : 0;
    }

    private ListReviewResponse listReviewResponse(List<Review> reviews, String message, Long productId) {
        ListReviewResponse listReviewResponse = new ListReviewResponse();
        listReviewResponse.setReviewDTOs(ReviewDTO.convertEntitiesToDTOs(reviews));
        listReviewResponse.setCount(reviews.size());
        listReviewResponse.setProductId(productId);
        listReviewResponse.setMessage(message);
        listReviewResponse.setStatus("OK");
        listReviewResponse.setCode(200);
        listReviewResponse.setAverageRating(averageRating(reviews));

        return listReviewResponse;
    }

}
