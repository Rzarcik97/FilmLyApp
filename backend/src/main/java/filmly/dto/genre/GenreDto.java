package filmly.dto.genre;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GenreDto(
        @JsonProperty("id") Long genreId,
        String name
) {}
