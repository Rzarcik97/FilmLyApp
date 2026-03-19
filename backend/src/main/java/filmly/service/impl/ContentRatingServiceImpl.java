package filmly.service.impl;

import filmly.model.ContentRating;
import filmly.service.ContentRatingService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContentRatingServiceImpl implements ContentRatingService {
    // TODO implement logic, change return to DTO, do Exceptions
    @Override
    public ContentRating getRatingById(Long id) {
        return null;
    }

    @Override
    public List<ContentRating> getAllByUserId(Long userId) {
        return List.of();
    }

    @Override
    public List<ContentRating> getAllByContentId(Long contentId) {
        return List.of();
    }

    @Override
    public ContentRating createRating(ContentRating contentRating) {
        return null;
    }

    @Override
    public ContentRating updateRating(Long id, ContentRating contentRating) {
        return null;
    }

    @Override
    public void deleteRating(Long id) {

    }
}
