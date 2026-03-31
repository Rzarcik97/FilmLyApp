package filmly.service;

import filmly.dto.content.ContentDto;
import filmly.dto.search.DiscoverRequest;
import filmly.dto.search.PagedResponse;
import filmly.model.Content;

public interface SearchService {
    PagedResponse<ContentDto> search(String title, Content.ContentType type, Integer page);

    PagedResponse<ContentDto> discover(DiscoverRequest request);
}
