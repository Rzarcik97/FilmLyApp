package filmly.dto.content;

import com.fasterxml.jackson.annotation.JsonProperty;
import filmly.dto.genre.GenreDto;
import java.util.List;

public record MovieDetailDto(
        Long id,
        String title,
        String overview,
        String status,
        @JsonProperty("release_date") String releaseDate,
        Integer runtime,
        Double popularity,
        @JsonProperty("vote_average") Double voteAverage,
        @JsonProperty("vote_count") Integer voteCount,
        @JsonProperty("poster_path") String posterPath,
        @JsonProperty("backdrop_path") String backdropPath,
        List<GenreDto> genres,
        List<CastDto> cast
) {}
