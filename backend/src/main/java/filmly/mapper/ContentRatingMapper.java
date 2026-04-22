package filmly.mapper;

import filmly.config.MapperConfig;
import filmly.dto.contentrating.ContentRatingResponseDto;
import filmly.model.ContentRating;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface ContentRatingMapper {

    @Mapping(expression = "java(contentRating.getUser().getUsernameField())",
            target = "username")
    @Mapping(source = "user.avatarUrl", target = "avatarUrl")
    ContentRatingResponseDto toDto(ContentRating contentRating);
}
