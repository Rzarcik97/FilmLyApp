package filmly.dto.content;

import filmly.dto.genre.GenreDto;
import filmly.model.Content;
import java.util.List;

public record MovieDto(
        Long contentId,
        String title,
        String posterPath,
        List<GenreDto> genres,
        Content.ContentType type
) implements ContentDto {}
