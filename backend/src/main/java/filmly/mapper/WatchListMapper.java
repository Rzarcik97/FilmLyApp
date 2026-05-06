package filmly.mapper;

import filmly.config.MapperConfig;
import filmly.dto.watchlist.WatchListResponseDto;
import filmly.model.WatchList;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = {GenreMapper.class})
public interface WatchListMapper {

    @Mapping(source = "content.externalId", target = "contentId")
    @Mapping(source = "content.type", target = "contentType")
    @Mapping(source = "content.title", target = "title")
    @Mapping(source = "content.posterPath", target = "posterPath")
    @Mapping(source = "content.genres", target = "genres")
    @Mapping(source = "content.releaseDate", target = "releaseDate")
    @Mapping(source = "content.voteAverage", target = "voteAverage")
    @Mapping(source = "content.voteCount", target = "voteCount")
    WatchListResponseDto toDto(WatchList watchList);

    List<WatchListResponseDto> toDtoList(List<WatchList> watchList);
}
