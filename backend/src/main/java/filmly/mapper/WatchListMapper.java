package filmly.mapper;

import filmly.config.MapperConfig;
import filmly.dto.content.ContentDto;
import filmly.dto.watchlist.WatchListResponseDto;
import filmly.model.WatchList;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = {GenreMapper.class})
public interface WatchListMapper {

    @Mapping(source = "watchList.content.externalId", target = "contentId")
    @Mapping(source = "watchList.content.type", target = "type")
    @Mapping(source = "watchList.content.title", target = "title")
    @Mapping(source = "watchList.content.posterPath", target = "posterPath")
    @Mapping(source = "watchList.content.genres", target = "genres")
    @Mapping(source = "watchList.content.releaseDate", target = "releaseDate")
    @Mapping(source = "watchList.content.voteAverage", target = "voteAverage")
    @Mapping(source = "watchList.content.voteCount", target = "voteCount")
    @Mapping(source = "likes", target = "likes")
    @Mapping(source = "dislikes", target = "dislikes")
    ContentDto toDtoWithLikes(WatchList watchList, Long likes, Long dislikes);

    @Mapping(source = "content.externalId", target = "contentId")
    @Mapping(source = "content.type", target = "contentType")
    @Mapping(source = "content.title", target = "title")
    @Mapping(source = "content.posterPath", target = "posterPath")
    @Mapping(source = "content.genres", target = "genres")
    @Mapping(source = "content.releaseDate", target = "releaseDate")
    @Mapping(source = "content.voteAverage", target = "voteAverage")
    @Mapping(source = "content.voteCount", target = "voteCount")
    WatchListResponseDto toDto(WatchList watchList);
}
