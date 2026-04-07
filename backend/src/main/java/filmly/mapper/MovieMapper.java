package filmly.mapper;

import filmly.config.MapperConfig;
import filmly.dto.content.ContentDto;
import filmly.dto.content.MovieDetailDto;
import filmly.dto.genre.GenreDto;
import filmly.dto.tmdb.TmdbContentResult;
import filmly.dto.tmdb.TmdbMovieDetailResponse;
import filmly.dto.tmdb.TmdbVideoResult;
import filmly.dto.tmdb.TmdbVideosResponse;
import filmly.service.GenreService;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(config = MapperConfig.class)
public abstract class MovieMapper {

    @Autowired
    protected GenreService genreService;

    @Mapping(target = "type", constant = "MOVIE")
    @Mapping(source = "id", target = "contentId")
    @Mapping(source = "genreIds", target = "genres")
    public abstract ContentDto toDto(TmdbContentResult result);

    @Mapping(target = "type", constant = "MOVIE")
    @Mapping(source = "id", target = "contentId")
    @Mapping(source = "genreIds", target = "genres")
    public abstract ContentDto fromContentResult(TmdbContentResult result);

    @Mapping(target = "type", constant = "MOVIE")
    @Mapping(source = "videos", target = "trailerKey")
    public abstract MovieDetailDto toDetailDto(TmdbMovieDetailResponse response);

    protected String extractTrailerKey(TmdbVideosResponse videos) {
        if (videos == null || videos.results() == null) {
            return null;
        }
        return videos.results().stream()
                .filter(v -> "YouTube".equals(v.site())
                        && "Trailer".equals(v.type())
                        && v.official())
                .map(TmdbVideoResult::key)
                .findFirst()
                .orElse(null);
    }

    protected List<GenreDto> longListToGenreDtoList(List<Long> genreIds) {
        if (genreIds == null) {
            return List.of();
        }
        List<GenreDto> result = new ArrayList<>();
        genreIds.forEach(id -> result.add(genreService.getGenreById(id)));
        return result;
    }
}
