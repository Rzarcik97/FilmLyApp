package filmly.dto.contentrating;

import filmly.model.Content;
import java.time.LocalDateTime;

public record ContentRatingResponseDto(
        Long id,
        Long contentId,
        Content.ContentType contentType,
        Float rating,
        String review,
        String username,
        String avatarUrl,
        LocalDateTime createdAt
) {}
