package filmly.mapper;

import filmly.config.MapperConfig;
import filmly.dto.content.MovieDto;
import filmly.dto.genre.GenreDto;
import filmly.dto.tmdb.TmdbMovieResult;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface MovieMapper {
    @Mapping(source = "id", target = "contentId")
    @Mapping(source = "posterPath", target = "posterPath")
    @Mapping(target = "genres", ignore = true) // TODO: podpinamy jak będzie GenreService
    MovieDto toDto(TmdbMovieResult result);

    default List<GenreDto> mapGenres(List<Long> genreIds) {
        // TODO: podpinamy GenreService gdy będzie gotowy
        return List.of();
    }
}
