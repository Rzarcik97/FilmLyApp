package filmly.dto.contentrating;

import java.time.LocalDateTime;

public record ContentRatingResponseDto(
        Long contentRatingId,
        Long contentId,
        Long userId,
        Double rating,
        LocalDateTime createdAt
) {}
