package filmly.service.impl;

import filmly.model.Review;
import filmly.service.ReviewService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    // TODO implement logic, change return to DTO, do Exceptions
    @Override
    public Review getReviewById(Long id) {
        return null;
    }

    @Override
    public List<Review> getAllByUserId(Long userId) {
        return List.of();
    }

    @Override
    public List<Review> getAllByContentId(Long contentId) {
        return List.of();
    }

    @Override
    public Review createReview(Review review) {
        return null;
    }

    @Override
    public Review updateReview(Long id, Review review) {
        return null;
    }

    @Override
    public void deleteReview(Long id) {

    }
}
