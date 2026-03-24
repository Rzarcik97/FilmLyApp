package filmly.mapper;

import filmly.config.MapperConfig;
import filmly.dto.genre.GenreDto;
import filmly.model.Genre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface GenreMapper {
    @Mapping(source = "genreId", target = "id")
    Genre toEntity(GenreDto dto);

    @Mapping(source = "id", target = "genreId")
    GenreDto toDto(Genre genre);

}
