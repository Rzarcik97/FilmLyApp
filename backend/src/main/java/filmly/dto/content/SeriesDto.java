package filmly.dto.content;

import filmly.dto.genre.GenreDto;
import java.util.List;

public record SeriesDto(
        Long contentId,
        String title,
        String posterPath,
        Integer numberOfSeasons,
        Integer numberOfEpisodes,
        List<GenreDto> genres
) {}
