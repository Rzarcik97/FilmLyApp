package filmly.mapper;

import filmly.config.MapperConfig;
import filmly.dto.content.ContentDto;
import filmly.model.ContentLike;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = {GenreMapper.class})
public interface ContentLikeMapper {

    @Mapping(source = "contentLike.content.externalId", target = "contentId")
    @Mapping(source = "contentLike.content.type", target = "type")
    @Mapping(source = "contentLike.content.title", target = "title")
    @Mapping(source = "contentLike.content.posterPath", target = "posterPath")
    @Mapping(source = "contentLike.content.genres", target = "genres")
    @Mapping(source = "contentLike.content.releaseDate", target = "releaseDate")
    @Mapping(source = "contentLike.content.voteAverage", target = "voteAverage")
    @Mapping(source = "contentLike.content.voteCount", target = "voteCount")
    @Mapping(source = "likes", target = "likes")
    @Mapping(source = "dislikes", target = "dislikes")
    ContentDto toDto(ContentLike contentLike, Long likes, Long dislikes);
}
