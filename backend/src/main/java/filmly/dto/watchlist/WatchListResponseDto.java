package filmly.dto.watchlist;

import filmly.model.Content;
import java.time.LocalDateTime;

public record WatchListResponseDto(
        Long id,
        Long contentId,
        Content.ContentType contentType,
        String title,
        String posterPath,
        LocalDateTime watchedAt,
        LocalDateTime addedAt
) {}
