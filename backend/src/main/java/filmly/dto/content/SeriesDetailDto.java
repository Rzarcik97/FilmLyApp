package filmly.dto.content;

import com.fasterxml.jackson.annotation.JsonProperty;
import filmly.dto.genre.GenreDto;
import filmly.dto.tmdb.TmdbProductionCountryDto;
import filmly.model.Content;
import java.util.List;

public record SeriesDetailDto(
        Long id,
        String title,
        Content.ContentType type,
        String tagLine,
        String overview,
        String status,
        @JsonProperty("release_date") String releaseDate,
        Integer numberOfEpisodes,
        Integer numberOfSeasons,
        Double popularity,
        @JsonProperty("vote_average") Double voteAverage,
        @JsonProperty("vote_count") Integer voteCount,
        String trailerKey,
        @JsonProperty("poster_path") String posterPath,
        @JsonProperty("backdrop_path") String backdropPath,
        @JsonProperty("production_countries") List<TmdbProductionCountryDto> productionCountries,
        List<GenreDto> genres
) {}
