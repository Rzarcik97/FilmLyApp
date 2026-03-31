package filmly.service.impl;

import filmly.dto.content.ContentDto;
import filmly.dto.search.DiscoverRequest;
import filmly.dto.search.PagedResponse;
import filmly.dto.tmdb.TmdbContentResponse;
import filmly.mapper.MovieMapper;
import filmly.mapper.SeriesMapper;
import filmly.model.Content;
import filmly.service.SearchService;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final RestClient restClient;

    private final MovieMapper movieMapper;

    private final SeriesMapper seriesMapper;

    @Override
    public PagedResponse<ContentDto> search(String title, Content.ContentType type, Integer page) {

        String endpoint = switch (type) {
            case MOVIE -> "/search/movie";
            case SERIES -> "/search/tv";
            case null -> "/search/multi";
        };

        TmdbContentResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(endpoint)
                        .queryParam("query", title)
                        .queryParam("page", page)
                        .build()
                )
                .retrieve()
                .body(TmdbContentResponse.class);

        if (response == null || response.results() == null) {
            return new PagedResponse<>(List.of(), 0, 0, 0);
        }

        return new PagedResponse<>(response.results().stream()
                .filter(result -> !"person".equals(result.mediaType()))
                .map(result -> {
                    if (type == Content.ContentType.SERIES || "tv".equals(result.mediaType())) {
                        return seriesMapper.fromContentResult(result);
                    }
                    return movieMapper.fromContentResult(result);
                })
                .filter(Objects::nonNull)
                .toList(),
                response.page(),
                response.totalPages(),
                response.totalResults());
    }

    @Override
    public PagedResponse<ContentDto> discover(DiscoverRequest request) {

        TmdbContentResponse response = restClient.get()
                .uri(buildDiscoverUri(request))
                .retrieve()
                .body(TmdbContentResponse.class);

        if (response == null || response.results() == null) {
            return new PagedResponse<>(List.of(), 0, 0, 0);
        }

        return new PagedResponse<>(response.results().stream()
                .map(result -> request.getType() == Content.ContentType.SERIES
                        ? seriesMapper.fromContentResult(result)
                        : movieMapper.fromContentResult(result))
                .toList(),
                response.page(),
                response.totalPages(),
                response.totalResults());
    }

    private String buildDiscoverUri(DiscoverRequest request) {
        String endpoint = request.getType() == Content.ContentType.SERIES
                ? "/discover/tv"
                : "/discover/movie";

        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(endpoint)
                .queryParam("page", request.getPage());

        if (request.getRatingMin() != null) {
            builder.queryParam("vote_average.gte", request.getRatingMin());
        }
        if (request.getRatingMax() != null) {
            builder.queryParam("vote_average.lte", request.getRatingMax());
        }
        if (request.getDateFrom() != null) {
            if (request.getType() == Content.ContentType.SERIES) {
                builder.queryParam("first_air_date.gte", request.getDateFrom());
            } else {
                builder.queryParam("primary_release_date.gte", request.getDateFrom());
            }
        }
        if (request.getDateTo() != null) {
            if (request.getType() == Content.ContentType.SERIES) {
                builder.queryParam("first_air_date.lte", request.getDateTo());
            } else {
                builder.queryParam("primary_release_date.lte", request.getDateTo());
            }
        }
        if (request.getSortBy() != null) {
            String sortBy = request.getSortBy().getValue();
            if (request.getType() == Content.ContentType.SERIES) {
                sortBy = sortBy.replace("release_date", "first_air_date");
            }
            builder.queryParam("sort_by", sortBy);
        }
        if (request.getGenreIds() != null && !request.getGenreIds().isEmpty()) {
            builder.queryParam("with_genres", request.getGenreIds().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(",")));
        }

        return builder.build().toUriString();
    }
}
