package filmly.dto.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import filmly.dto.genre.GenreDto;
import java.util.List;

public record TmdbMovieDetailResponse(
        Long id,
        String title,
        @JsonProperty("tagline")String tagLine,
        String overview,
        String status,
        @JsonProperty("release_date") String releaseDate,
        Integer runtime,
        Double popularity,
        @JsonProperty("vote_average") Double voteAverage,
        @JsonProperty("vote_count") Integer voteCount,
        @JsonProperty("poster_path") String posterPath,
        @JsonProperty("backdrop_path") String backdropPath,
        @JsonProperty("production_countries") List<TmdbProductionCountryDto> productionCountries,
        List<GenreDto> genres,
        TmdbCreditsResponse credits,
        TmdbVideosResponse videos
) {}
