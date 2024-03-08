package hcmute.kltn.vtv.controller.guest;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.guest.ListReviewResponse;
import hcmute.kltn.vtv.model.data.user.response.ReviewResponse;
import hcmute.kltn.vtv.service.guest.IReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {

    @Autowired
    private IReviewService reviewService;

    @GetMapping("/product/{productId}")
    public ResponseEntity<ListReviewResponse> getReviewsByProductId(@PathVariable Long productId) {
        if (productId == null) {
            throw new BadRequestException("Mã sản phẩm không được để trống!");
        }
        return ResponseEntity.ok(reviewService.getReviewsByProductId(productId));
    }

    @GetMapping("/product/{productId}/rating/{rating}")
    public ResponseEntity<ListReviewResponse> getReviewsByProductIdAndRating(@PathVariable Long productId,
            @PathVariable int rating) {
        if (productId == null) {
            throw new BadRequestException("Mã sản phẩm không được để trống!");
        }
        if (rating < 1 || rating > 5) {
            throw new BadRequestException("Xếp hạng không hợp lệ!");
        }
        return ResponseEntity.ok(reviewService.getReviewsByProductIdAndRating(productId, rating));
    }

    @GetMapping("/product/{productId}/image")
    public ResponseEntity<ListReviewResponse> getReviewsByProductIdAndImageNotNull(@PathVariable Long productId) {
        if (productId == null) {
            throw new BadRequestException("Mã sản phẩm không được để trống!");
        }
        return ResponseEntity.ok(reviewService.getReviewsByProductIdAndImageNotNull(productId));
    }

    @GetMapping("/detail/{reviewId}")
    public ResponseEntity<ReviewResponse> getReviewDetailById(@PathVariable UUID reviewId) {
        if (reviewId == null) {
            throw new BadRequestException("Mã đánh giá không được để trống!");
        }
        return ResponseEntity.ok(reviewService.getReviewDetailById(reviewId));
    }

    @GetMapping("/count/{productId}")
    public ResponseEntity<Integer> countReviewByProductId(@PathVariable Long productId) {
        if (productId == null) {
            throw new BadRequestException("Mã sản phẩm không được để trống!");
        }
        return ResponseEntity.ok(reviewService.countReviewByProductId(productId));
    }

    @GetMapping("/avg-rating/{productId}")
    public ResponseEntity<Float> getAvgRatingByProductId(@PathVariable Long productId) {
        if (productId == null) {
            throw new BadRequestException("Mã sản phẩm không được để trống!");
        }
        return ResponseEntity.ok(reviewService.countAverageRatingByProductId(productId));
    }

}
