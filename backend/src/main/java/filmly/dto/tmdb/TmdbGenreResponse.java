package filmly.dto.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import filmly.dto.genre.GenreDto;
import java.util.List;

public record TmdbGenreResponse(
        @JsonProperty("genres") List<GenreDto> genres
) {}
