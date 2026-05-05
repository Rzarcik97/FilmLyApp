package filmly.dto.content;

import filmly.dto.tmdb.TmdbContentResult;

public record ScoreContent(
        TmdbContentResult result,
        double score
) {}
