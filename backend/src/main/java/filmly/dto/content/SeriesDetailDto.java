package filmly.dto.content;

import com.fasterxml.jackson.annotation.JsonProperty;
import filmly.dto.genre.GenreDto;
import java.util.List;

public record SeriesDetailDto(
        Long id,
        String title,
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
        List<GenreDto> genres
) {}
