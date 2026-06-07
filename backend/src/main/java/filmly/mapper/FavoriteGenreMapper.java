package filmly.mapper;

import filmly.config.MapperConfig;
import filmly.dto.favoritegenres.FavoriteGenreResponseDto;
import filmly.model.FavoriteGenre;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface FavoriteGenreMapper {

    @Mapping(source = "genre.name", target = "genreName")
    @Mapping(source = "genre.imagePath", target = "genreImagePath")
    @Mapping(source = "genre.type", target = "genreType")
    FavoriteGenreResponseDto toDto(FavoriteGenre favoriteGenre);

    List<FavoriteGenreResponseDto> toDtoList(List<FavoriteGenre> favoriteGenres);
}
