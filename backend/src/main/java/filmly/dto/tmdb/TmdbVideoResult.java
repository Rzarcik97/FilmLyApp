package filmly.dto.tmdb;

public record TmdbVideoResult(
        String key,
        String site,
        String type,
        boolean official
) {}
