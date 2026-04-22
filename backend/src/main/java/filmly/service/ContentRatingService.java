package filmly.service;

import filmly.dto.contentrating.ContentRatingRequestDto;
import filmly.dto.contentrating.ContentRatingResponseDto;
import filmly.dto.contentrating.ContentRatingUpdateRequestDto;
import filmly.model.Content;
import java.util.List;

public interface ContentRatingService {

    List<ContentRatingResponseDto> getByContentId(Long contentId, Content.ContentType contentType);

    ContentRatingResponseDto addRating(String email, ContentRatingRequestDto dto);

    ContentRatingResponseDto updateRating(String email, ContentRatingUpdateRequestDto dto);

    void deleteRating(String email, Long id);
}
