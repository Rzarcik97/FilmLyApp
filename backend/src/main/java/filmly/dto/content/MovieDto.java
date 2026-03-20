package filmly.dto.content;

import filmly.dto.genre.GenreDto;
import java.util.List;

public record MovieDto(
        Long contentId,
        String title,
        String posterPath,
        List<GenreDto> genres
) {}
