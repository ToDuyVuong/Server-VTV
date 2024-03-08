package hcmute.kltn.vtv.service.user;

import hcmute.kltn.vtv.model.data.user.request.ReviewRequest;
import hcmute.kltn.vtv.model.data.user.response.ReviewResponse;
import hcmute.kltn.vtv.model.entity.user.Review;

import java.util.UUID;

public interface IReviewCustomerService {
    ReviewResponse addNewReview(ReviewRequest request, String username);

    ReviewResponse getReviewByOrderItemId(UUID orderItemId);

    ReviewResponse deleteReview(UUID reviewId, String username);

    Review checkReviewRole(UUID reviewId, String username, boolean isShop);

    Review checkReview(UUID reviewId);
}
