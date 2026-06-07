package filmly.dto.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TmdbProductionCountryDto(
        @JsonProperty("iso_3166_1") String iso,
        String name
) {}
