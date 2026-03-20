package filmly.dto.review;

import java.time.LocalDateTime;

public record ReviewResponseDto(
        Long ReviewResponseId,
        Long contentId,
        String authorName,
        String reviewText,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
