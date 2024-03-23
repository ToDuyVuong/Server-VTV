package hcmute.kltn.vtv.controller.user;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.model.data.user.request.ReviewRequest;
import hcmute.kltn.vtv.model.data.user.response.ReviewResponse;
import hcmute.kltn.vtv.repository.user.ReviewRepository;
import hcmute.kltn.vtv.service.user.IReviewCustomerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/customer/review")
@RequiredArgsConstructor
public class ReviewCustomerController {

    @Autowired
    private IReviewCustomerService reviewService;

    @Autowired
    private ReviewRepository reviewRepository;

    @PostMapping("/add")
    public ResponseEntity<ReviewResponse> addNewReview(@ModelAttribute ReviewRequest reviewRequest,
                                                       HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        reviewRequest.validate();

        return ResponseEntity.ok(reviewService.addNewReview(reviewRequest, username));
    }


    @GetMapping("/detail/by-order-item/{orderItemId}")
    public ResponseEntity<ReviewResponse> getReviewDetailByOrderItemId(@PathVariable UUID orderItemId) {

        return ResponseEntity.ok(reviewService.getReviewByOrderItemId(orderItemId));
    }


    @PatchMapping("/delete/{reviewId}")
    public ResponseEntity<ReviewResponse> deleteReview(@PathVariable UUID reviewId,
                                                       HttpServletRequest request) {

        String username = (String) request.getAttribute("username");
        return ResponseEntity.ok(reviewService.deleteReview(reviewId, username));
    }


    @GetMapping("/exist/by-order-item/{orderItemId}")
    public ResponseEntity<Boolean> checkReviewExist(@PathVariable UUID orderItemId) {

        return ResponseEntity.ok(reviewRepository.existsByOrderItemOrderItemId(orderItemId));
    }

}
