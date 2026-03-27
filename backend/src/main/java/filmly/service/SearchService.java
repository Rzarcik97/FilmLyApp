package filmly.service;

import filmly.dto.content.ContentDto;
import filmly.model.Content;
import java.util.List;

public interface SearchService {
    List<ContentDto> search(String title, Content.ContentType type);

    List<ContentDto> discover(Double ratingMin,
                              Double ratingMax,
                              String dateFrom,
                              String dateTo,
                              List<Long> genreIds,
                              String type,
                              String sortBy,
                              Integer page);
}
