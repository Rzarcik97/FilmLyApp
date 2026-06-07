package filmly.dto.tmdb;

import filmly.dto.content.ActorDto;
import java.util.List;

public record TmdbPersonResponse(
        List<ActorDto> results) {
}
