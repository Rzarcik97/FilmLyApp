package filmly.mapper;

import filmly.config.MapperConfig;
import filmly.dto.content.ContentDto;
import filmly.dto.genre.GenreDto;
import filmly.dto.tmdb.TmdbContentResult;
import filmly.service.GenreService;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(config = MapperConfig.class)
public abstract class SeriesMapper {
    @Autowired
    protected GenreService genreService;

    @Mapping(target = "type", constant = "SERIES")
    @Mapping(source = "id", target = "contentId")
    @Mapping(source = "name", target = "title")
    @Mapping(source = "genreIds", target = "genres")
    @Mapping(source = "firstAirDate", target = "releaseDate")
    public abstract ContentDto fromContentResult(TmdbContentResult result);

    protected List<GenreDto> longListToGenreDtoList(List<Long> genreIds) {
        if (genreIds == null) {
            return List.of();
        }
        List<GenreDto> result = new ArrayList<>();
        genreIds.forEach(id -> result.add(genreService.getGenreById(id)));
        return result;
    }
}
