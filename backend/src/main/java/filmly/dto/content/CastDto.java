package filmly.dto.content;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CastDto(
        Long id,
        String name,
        String character,
        @JsonProperty("profile_path") String profilePath,
        Integer order
) {}
