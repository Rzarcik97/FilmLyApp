package filmly.mapper;

import filmly.config.MapperConfig;
import filmly.dto.content.CastDto;
import filmly.dto.content.MovieDetailDto;
import filmly.dto.content.MovieDto;
import filmly.dto.genre.GenreDto;
import filmly.dto.tmdb.TmdbMovieDetailResponse;
import filmly.dto.tmdb.TmdbMovieResult;
import filmly.service.GenreService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(config = MapperConfig.class)
public abstract class MovieMapper {

    @Autowired
    protected GenreService genreService;

    @Mapping(source = "id", target = "contentId")
    @Mapping(source = "posterPath", target = "posterPath")
    @Mapping(source = "genreIds", target = "genres")
    public abstract MovieDto toDto(TmdbMovieResult result);

    @Mapping(source = "credits.cast", target = "cast")
    public abstract MovieDetailDto toDetailDto(TmdbMovieDetailResponse response);

    protected List<CastDto> limitCast(List<CastDto> cast) {
        if (cast == null) {
            return List.of();
        }
        return cast.stream()
                .sorted(Comparator.comparingInt(CastDto::order))
                .limit(10)
                .toList();
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
