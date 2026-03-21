package filmly.dto.contentrating;

import java.time.LocalDateTime;

public record ContentRatingResponseDto(
        Long contentRatingId,
        Long contentId,
        Long userId,
        Float rating,
        LocalDateTime createdAt
) {}
