package filmly.dto.watchlist;

import filmly.dto.genre.GenreDto;
import filmly.model.Content;
import java.time.LocalDateTime;
import java.util.List;

public record WatchListResponseDto(
        Long id,
        Long contentId,
        Content.ContentType contentType,
        String title,
        String posterPath,
        List<GenreDto> genres,
        String releaseDate,
        Double voteAverage,
        Integer voteCount,
        Long likes,
        Long dislikes,
        LocalDateTime watchedAt,
        LocalDateTime addedAt
) {}
