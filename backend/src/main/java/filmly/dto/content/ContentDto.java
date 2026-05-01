package filmly.dto.content;

import com.fasterxml.jackson.annotation.JsonProperty;
import filmly.dto.genre.GenreDto;
import filmly.model.Content;
import java.util.List;

public record ContentDto(
        Long contentId,
        Content.ContentType type,
        String title,
        @JsonProperty("poster_path")String posterPath,
        List<GenreDto> genres,
        @JsonProperty("release_date")String releaseDate,
        @JsonProperty("vote_average")Double voteAverage,
        @JsonProperty("vote_count")Integer voteCount,
        Long likes,
        Long dislikes
) {}

