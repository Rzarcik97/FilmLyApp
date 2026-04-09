package filmly.dto.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import filmly.dto.genre.GenreDto;
import java.util.List;

public record TmdbSeriesDetailResponse(
        Long id,
        String name,
        @JsonProperty("tagline")String tagLine,
        String overview,
        String status,
        @JsonProperty("first_air_date")String firstAirDate,
        @JsonProperty("number_of_episodes")Integer numberOfEpisodes,
        @JsonProperty("number_of_seasons")Integer numberOfSeasons,
        Double popularity,
        @JsonProperty("vote_average") Double voteAverage,
        @JsonProperty("vote_count") Integer voteCount,
        String trailerKey,
        @JsonProperty("poster_path") String posterPath,
        @JsonProperty("backdrop_path") String backdropPath,
        @JsonProperty("production_countries") List<TmdbProductionCountryDto> productionCountries,
        List<GenreDto> genres,
        TmdbVideosResponse videos
) {}

