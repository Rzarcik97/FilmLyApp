package filmly.dto.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record TmdbMovieResult(
        Long id,
        String title,
        @JsonProperty("poster_path") String posterPath,
        @JsonProperty("genre_ids") List<Long> genreIds,
        @JsonProperty("vote_average") Double voteAverage,
        @JsonProperty("release_date") String releaseDate,
        String overview
) {}
