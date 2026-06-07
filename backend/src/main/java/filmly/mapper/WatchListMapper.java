package filmly.mapper;

import filmly.config.MapperConfig;
import filmly.dto.watchlist.WatchListResponseDto;
import filmly.model.WatchList;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = {GenreMapper.class})
public interface WatchListMapper {

    @Mapping(source = "watchList.content.externalId", target = "contentId")
    @Mapping(source = "watchList.content.type", target = "contentType")
    @Mapping(source = "watchList.content.title", target = "title")
    @Mapping(source = "watchList.content.posterPath", target = "posterPath")
    @Mapping(source = "watchList.content.genres", target = "genres")
    @Mapping(source = "watchList.content.releaseDate", target = "releaseDate")
    @Mapping(source = "watchList.content.voteAverage", target = "voteAverage")
    @Mapping(source = "watchList.content.voteCount", target = "voteCount")
    @Mapping(source = "likes", target = "likes")
    @Mapping(source = "dislikes", target = "dislikes")
    WatchListResponseDto toDtoWithLikes(WatchList watchList, Long likes, Long dislikes);
}
