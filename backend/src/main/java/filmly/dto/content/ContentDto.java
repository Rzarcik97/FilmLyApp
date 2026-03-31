package filmly.dto.content;

import filmly.dto.genre.GenreDto;
import filmly.model.Content;
import java.util.List;

public record ContentDto(
        Long contentId,
        Content.ContentType type,
        String title,
        String posterPath,
        List<GenreDto> genres,
        String releaseDate,
        String voteAverage,
        String voteCount) {}

