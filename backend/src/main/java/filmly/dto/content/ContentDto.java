package filmly.dto.content;

import filmly.dto.genre.GenreDto;
import java.time.LocalDateTime;
import java.util.List;

public record ContentDto(
        Long contentId,
        String externalId,
        String type,
        LocalDateTime createdAt,
        List<GenreDto> genres
) {}
