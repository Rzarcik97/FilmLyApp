package filmly.dto.tmdb;

import java.util.List;

public record TmdbVideosResponse(
        List<TmdbVideoResult> results
) {}
