package hcmute.kltn.vtv.model.data.user.response;

import hcmute.kltn.vtv.model.dto.user.ReviewDTO;
import hcmute.kltn.vtv.model.entity.user.Review;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse extends ResponseAbstract {

    private Long productId;
    private ReviewDTO reviewDTO;


    public static ReviewResponse reviewResponse(Review review, String message, String status) {
        ReviewResponse reviewResponse = new ReviewResponse();
        reviewResponse.setReviewDTO(ReviewDTO.convertEntityToDTO(review));
        reviewResponse.setMessage(message);
        reviewResponse.setProductId(review.getProduct().getProductId());
        reviewResponse.setStatus(status);
        reviewResponse.setCode(200);

        return reviewResponse;
    }


}
