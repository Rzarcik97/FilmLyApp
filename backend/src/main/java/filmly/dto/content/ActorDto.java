package filmly.dto.content;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ActorDto(
        Long id,
        String name,
        @JsonProperty("profile_path") String profilePath
) {}
