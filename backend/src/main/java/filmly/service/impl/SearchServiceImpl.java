package filmly.service.impl;

import filmly.dto.content.ContentDto;
import filmly.dto.tmdb.TmdbContentResponse;
import filmly.mapper.ContentMapper;
import filmly.model.Content;
import filmly.service.SearchService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final RestClient restClient;

    private final ContentMapper contentMapper;

    @Override
    public List<ContentDto> search(String title, Content.ContentType type) {

        String endpoint;

        switch (type) {
            case MOVIE -> endpoint = "/search/movie";
            case SERIES -> endpoint = "/search/tv";
            default -> endpoint = "/search/multi";
        }

        TmdbContentResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(endpoint)
                        .queryParam("query", title)
                        .build()
                )
                .retrieve()
                .body(TmdbContentResponse.class);

        if (response == null || response.results() == null) {
            return List.of();
        }

        return response.results().stream()
                .map(contentMapper::toContentDto)
                .toList();
    }

    @Override
    public List<ContentDto> discover(Double ratingMin,
                                     Double ratingMax,
                                     String dateFrom,
                                     String dateTo,
                                     List<Long> genreIds,
                                     String type,
                                     String sortBy,
                                     Integer page) {
        return List.of();
    }
}
