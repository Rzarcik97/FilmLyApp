package filmly.mapper;

import filmly.dto.content.ContentDto;
import filmly.dto.tmdb.TmdbContentResult;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

//TODO {SeriesMapper.class}
@Mapper(componentModel = "spring", uses = {MovieMapper.class})
public abstract class ContentMapper {

    @Autowired
    protected MovieMapper movieMapper;

    //@Autowired
    //protected SeriesMapper seriesMapper;

    public ContentDto toContentDto(TmdbContentResult result) {
        return switch (result.mediaType()) {
            case "movie" -> movieMapper.fromContentResult(result);
            case null -> movieMapper.fromContentResult(result);
            //case "tv"    -> seriesMapper.fromContentResult(result);
            default -> null;
        };
    }
}
