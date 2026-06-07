package filmly.service;

import filmly.dto.content.ContentDto;
import filmly.dto.contentlikes.ContentLikeResponseDto;
import filmly.dto.search.DiscoverRequest;
import filmly.dto.search.PagedResponse;
import filmly.dto.tmdb.TmdbContentResponse;
import filmly.dto.tmdb.TmdbContentResult;
import filmly.mapper.MovieMapper;
import filmly.mapper.SeriesMapper;
import filmly.model.Content;
import filmly.service.impl.SearchServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private RestClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @Mock
    private MovieMapper movieMapper;

    @Mock
    private SeriesMapper seriesMapper;

    @Mock
    private ContentLikeService contentLikeService;

    @InjectMocks
    private SearchServiceImpl searchService;

    @BeforeEach
    void setUp() {
        mockRestClientChain();
    }

    private void mockRestClientChain() {

        when(restClient.get())
                .thenReturn(requestHeadersUriSpec);

        lenient().when(requestHeadersUriSpec.uri(anyString()))
                .thenReturn(requestHeadersSpec);

        lenient().when(requestHeadersUriSpec.uri(anyString(), any(Object[].class)))
                .thenReturn(requestHeadersSpec);

        lenient().when(requestHeadersUriSpec.uri(any(Function.class)))
                .thenReturn(requestHeadersSpec);

        when(requestHeadersSpec.retrieve())
                .thenReturn(responseSpec);
    }

    @Test
    @DisplayName("""
            search | should return movie results
            """)
    void search_movies_success() {
        // given
        TmdbContentResult movie = mock(TmdbContentResult.class);

        when(movie.id()).thenReturn(100L);
        when(movie.mediaType()).thenReturn("movie");

        TmdbContentResponse response =
                new TmdbContentResponse(
                        1,
                        List.of(movie),
                        1,
                        1
                );

        ContentDto dto = mock(ContentDto.class);

        when(responseSpec.body(TmdbContentResponse.class))
                .thenReturn(response);

        when(contentLikeService.getLikesByContentIds(
                List.of(100L),
                Content.ContentType.MOVIE
        )).thenReturn(Map.of(
                100L,
                new ContentLikeResponseDto(5L, 1L)
        ));

        when(contentLikeService.getLikesByContentIds(
                List.of(),
                Content.ContentType.SERIES
        )).thenReturn(Map.of());

        when(movieMapper.fromContentResult(movie, 5L, 1L))
                .thenReturn(dto);

        // when
        PagedResponse<ContentDto> result =
                searchService.search(
                        "Batman",
                        Content.ContentType.MOVIE,
                        1
                );

        // then
        assertEquals(1, result.results().size());
        assertEquals(dto, result.results().getFirst());
    }

    @Test
    @DisplayName("""
            search | should filter out persons
            """)
    void search_shouldFilterPersons() {
        // given
        TmdbContentResult person = mock(TmdbContentResult.class);
        when(person.mediaType()).thenReturn("person");

        TmdbContentResponse response =
                new TmdbContentResponse(
                        1,
                        List.of(person),
                        1,
                        1
                );

        when(responseSpec.body(TmdbContentResponse.class))
                .thenReturn(response);

        when(contentLikeService.getLikesByContentIds(
                anyList(),
                any()
        )).thenReturn(Map.of());

        // when
        PagedResponse<ContentDto> result =
                searchService.search(
                        "Tom",
                        null,
                        1
                );

        // then
        assertTrue(result.results().isEmpty());
    }

    @Test
    @DisplayName("""
            search | should return empty response
            when api response null
            """)
    void search_nullResponse() {
        // given
        when(responseSpec.body(TmdbContentResponse.class))
                .thenReturn(null);

        // when
        PagedResponse<ContentDto> result =
                searchService.search(
                        "Batman",
                        Content.ContentType.MOVIE,
                        1
                );

        // then
        assertTrue(result.results().isEmpty());

        assertEquals(0, result.page());
        assertEquals(0, result.totalPages());
        assertEquals(0, result.totalResults());
    }

    @Test
    @DisplayName("""
            discover | should return movie results
            """)
    void discover_movies_success() {
        // given
        DiscoverRequest request = new DiscoverRequest();
        request.setType(Content.ContentType.MOVIE);
        request.setPage(1);

        TmdbContentResult movie = mock(TmdbContentResult.class);

        when(movie.id()).thenReturn(100L);

        TmdbContentResponse response =
                new TmdbContentResponse(
                        1,
                        List.of(movie),
                        1,
                        1
                );

        ContentDto dto = mock(ContentDto.class);

        when(responseSpec.body(TmdbContentResponse.class))
                .thenReturn(response);

        when(contentLikeService.getLikesByContentIds(
                List.of(100L),
                Content.ContentType.MOVIE
        )).thenReturn(Map.of(
                100L,
                new ContentLikeResponseDto(10L, 2L)
        ));

        when(movieMapper.fromContentResult(movie, 10L, 2L))
                .thenReturn(dto);

        // when
        PagedResponse<ContentDto> result =
                searchService.discover(request);

        // then
        assertEquals(1, result.results().size());
        assertEquals(dto, result.results().get(0));
    }

    @Test
    @DisplayName("""
            discover | should return empty response
            when api response null
            """)
    void discover_nullResponse() {
        // given
        DiscoverRequest request = new DiscoverRequest();
        request.setType(Content.ContentType.MOVIE);

        when(responseSpec.body(TmdbContentResponse.class))
                .thenReturn(null);

        // when
        PagedResponse<ContentDto> result =
                searchService.discover(request);

        // then
        assertTrue(result.results().isEmpty());

        assertEquals(0, result.page());
        assertEquals(0, result.totalPages());
        assertEquals(0, result.totalResults());
    }

    @Test
    @DisplayName("""
            discover | should map series content
            """)
    void discover_series_success() {
        // given
        DiscoverRequest request = new DiscoverRequest();
        request.setType(Content.ContentType.SERIES);

        TmdbContentResult series = mock(TmdbContentResult.class);

        when(series.id()).thenReturn(200L);

        TmdbContentResponse response =
                new TmdbContentResponse(
                        1,
                        List.of(series),
                        1,
                        1
                );

        ContentDto dto = mock(ContentDto.class);

        when(responseSpec.body(TmdbContentResponse.class))
                .thenReturn(response);

        when(contentLikeService.getLikesByContentIds(
                List.of(200L),
                Content.ContentType.SERIES
        )).thenReturn(Map.of(
                200L,
                new ContentLikeResponseDto(7L, 0L)
        ));

        when(seriesMapper.fromContentResult(series, 7L, 0L))
                .thenReturn(dto);

        // when
        PagedResponse<ContentDto> result =
                searchService.discover(request);

        // then
        assertEquals(1, result.results().size());
        assertEquals(dto, result.results().getFirst());
    }
}
