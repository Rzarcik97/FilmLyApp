package filmly.dto.content;

import com.fasterxml.jackson.annotation.JsonProperty;
import filmly.dto.genre.GenreDto;
import filmly.model.Content;
import java.util.List;

public record MovieDetailDto(
        Long id,
        String title,
        Content.ContentType type,
        String tagLine,
        String overview,
        String status,
        @JsonProperty("release_date") String releaseDate,
        Integer runtime,
        Double popularity,
        @JsonProperty("vote_average") Double voteAverage,
        @JsonProperty("vote_count") Integer voteCount,
        String trailerKey,
        @JsonProperty("poster_path") String posterPath,
        @JsonProperty("backdrop_path") String backdropPath,
        List<GenreDto> genres
) {}
