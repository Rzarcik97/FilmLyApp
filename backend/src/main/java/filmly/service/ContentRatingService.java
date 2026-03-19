package filmly.service;

import filmly.model.ContentRating;
import java.util.List;

public interface ContentRatingService {
    ContentRating getRatingById(Long id);

    List<ContentRating> getAllByUserId(Long userId);

    List<ContentRating> getAllByContentId(Long contentId);

    ContentRating createRating(ContentRating contentRating);

    ContentRating updateRating(Long id, ContentRating contentRating);

    void deleteRating(Long id);
}
