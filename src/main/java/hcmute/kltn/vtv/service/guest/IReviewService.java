package hcmute.kltn.vtv.service.guest;

import hcmute.kltn.vtv.model.data.guest.ListReviewResponse;
import hcmute.kltn.vtv.model.data.user.response.ReviewResponse;

import java.util.UUID;

public interface IReviewService {
    ReviewResponse getReviewDetailById(UUID reviewId);

    ListReviewResponse getReviewsByProductId(Long productId);

    ListReviewResponse getReviewsByProductIdAndRating(Long productId, int rating);

    ListReviewResponse getReviewsByProductIdAndImageNotNull(Long productId);

    int countReviewByProductId(Long productId);

    float countAverageRatingByProductId(Long productId);
}
