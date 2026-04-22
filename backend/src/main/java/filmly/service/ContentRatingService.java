package filmly.service;

import filmly.dto.contentrating.ContentRatingRequestDto;
import filmly.dto.contentrating.ContentRatingResponseDto;

public interface ContentRatingService {

    ContentRatingResponseDto addRating(String email, ContentRatingRequestDto dto);

    ContentRatingResponseDto updateRating(String email, ContentRatingRequestDto dto);

    void deleteRating(String email, ContentRatingRequestDto dto);
}
