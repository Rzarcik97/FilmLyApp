package filmly.dto.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record TmdbContentResult(
        Long id,
        String title,
        @JsonProperty("name") String name, // TV series uses "name" instead of "title"
        @JsonProperty("poster_path") String posterPath,
        @JsonProperty("genre_ids") List<Long> genreIds,
        @JsonProperty("vote_average") Double voteAverage,
        @JsonProperty("vote_count") int voteCount,
        @JsonProperty("release_date") String releaseDate,
        @JsonProperty("first_air_date") String firstAirDate,
        String overview,
        @JsonProperty("media_type") String mediaType
) {}
