package filmly.dto.watchlist;

import java.time.LocalDateTime;

public record WatchlistResponseDto(
        Long watchlistId,
        Long contentId,
        LocalDateTime addedAt
) {}
