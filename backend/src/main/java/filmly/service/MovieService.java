package filmly.service;

import filmly.dto.content.ContentDto;
import filmly.dto.content.MovieDetailDto;
import java.util.List;

public interface MovieService extends TmdbContentService<ContentDto, MovieDetailDto> {
    List<ContentDto> findUpcoming();
}
