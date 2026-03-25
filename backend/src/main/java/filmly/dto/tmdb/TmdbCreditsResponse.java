package filmly.dto.tmdb;

import filmly.dto.content.CastDto;
import java.util.List;

public record TmdbCreditsResponse(
        List<CastDto> cast
) {}
