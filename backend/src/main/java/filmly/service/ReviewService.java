package filmly.service;

import filmly.model.Review;
import java.util.List;

public interface ReviewService {

    Review getReviewById(Long id);

    List<Review> getAllByUserId(Long userId);

    List<Review> getAllByContentId(Long contentId);

    Review createReview(Review review);

    Review updateReview(Long id, Review review);

    void deleteReview(Long id);
}
