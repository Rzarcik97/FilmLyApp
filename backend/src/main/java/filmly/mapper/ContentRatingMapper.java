package filmly.mapper;

import filmly.config.MapperConfig;
import filmly.dto.contentrating.ContentRatingResponseDto;
import filmly.model.ContentRating;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface ContentRatingMapper {

    ContentRatingResponseDto toDto(ContentRating contentRating);
}
