package filmly.dto.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record TmdbMovieResponse(
        int page,
        List<TmdbMovieResult> results,
        @JsonProperty("total_pages") int totalPages,
        @JsonProperty("total_results") int totalResults
) {}
