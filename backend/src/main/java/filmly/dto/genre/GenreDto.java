package filmly.dto.genre;

import com.fasterxml.jackson.annotation.JsonProperty;
import filmly.enums.GenreType;

public record GenreDto(
        @JsonProperty("id") Long genreId,
        String name,
        GenreType type,
        String imagePath
) {}
