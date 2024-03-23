package hcmute.kltn.vtv.repository.user;

import hcmute.kltn.vtv.model.entity.user.Review;
import hcmute.kltn.vtv.model.extra.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

    boolean existsByOrderItemOrderItemId(UUID orderItemId);

    boolean existsByReviewIdAndCustomerUsername(UUID reviewId, String username);


    boolean existsByReviewIdAndProductShopCustomerUsername(UUID reviewId, String username);

    boolean existsByReviewIdAndStatus(UUID reviewId, Status status);

    boolean existsByReviewId(UUID reviewId);

    boolean existsByOrderItemOrderItemIdAndCustomerUsername(UUID orderItemId, String username);

    Optional<Review> findByOrderItemOrderItemId(UUID orderItemId);

    Optional<Review> findByReviewIdAndStatus(UUID reviewId, Status status);

    Optional<List<Review>> findAllByProductProductIdAndStatus(Long productId, Status status);

    Optional<List<Review>> findAllByProductProductIdAndImageNotNull(Long productId);

    Optional<List<Review>> findAllByProductProductIdAndRatingAndStatus(Long productId, int rating, Status status);

    int countByProductProductId(Long productId);

}
