package filmly.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import filmly.dto.content.CastDto;
import filmly.dto.content.ContentDto;
import filmly.dto.contentlikes.ContentLikeResponseDto;
import filmly.dto.content.SeriesDetailDto;
import filmly.dto.tmdb.TmdbContentResponse;
import filmly.dto.tmdb.TmdbContentResult;
import filmly.dto.tmdb.TmdbCreditsResponse;
import filmly.dto.tmdb.TmdbSeriesDetailResponse;
import filmly.enums.GenreType;
import filmly.exception.EntityNotFoundException;
import filmly.mapper.SeriesMapper;
import filmly.model.Content;
import filmly.service.impl.RecommendationScorer;
import filmly.service.impl.SeriesServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

@ExtendWith(MockitoExtension.class)
public class SeriesServiceTest {

    @Mock
    private RestClient restClient;

    @Mock
    private SeriesMapper seriesMapper;

    @Mock
    private ContentLikeService contentLikeService;

    @Mock
    private FavoriteGenreService favoriteGenreService;

    @Mock
    private RecommendationScorer scorer;

    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private RestClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @InjectMocks
    private SeriesServiceImpl seriesServiceImpl;

    // ── helpers ───────────────────────────────────────────────────────────────

    private void mockRestClientChainBoth() {
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        lenient().when(requestHeadersUriSpec.uri(any(String.class)))
                .thenReturn(requestHeadersSpec);
        lenient().when(requestHeadersUriSpec.uri(any(String.class), any(Object[].class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    private TmdbContentResult buildContentResult(Long id) {
        return new TmdbContentResult(
                id,
                null,
                "Test Series " + id,
                "/poster.jpg",
                List.of(18L),
                7.5,
                100,
                null,
                "2024-01-01",
                "overview",
                "tv",
                50.0
        );
    }

    private ContentDto buildContentDto(Long id) {
        return new ContentDto(
                id,
                Content.ContentType.SERIES,
                "Test Series " + id,
                "/poster.jpg",
                List.of(),
                "2024-01-01",
                7.5,
                100,
                0L,
                0L
        );
    }

    private ContentLikeResponseDto noLikes() {
        return new ContentLikeResponseDto(0L, 0L);
    }

    // ── findPopular ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("""
            findPopular | Returns list of ContentDto with likes
            """)
    void findPopular_ShouldReturnContentDtoList() {
        // Given
        TmdbContentResult result = buildContentResult(1L);
        TmdbContentResponse response = new TmdbContentResponse(1, List.of(result), 1, 1);
        ContentDto dto = buildContentDto(1L);

        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(TmdbContentResponse.class)).thenReturn(response);
        when(contentLikeService.getLikesByContentIds(List.of(1L), Content.ContentType.SERIES))
                .thenReturn(Map.of(1L, noLikes()));
        when(seriesMapper.toDto(result, 0L, 0L)).thenReturn(dto);

        // When
        List<ContentDto> results = seriesServiceImpl.findPopular();

        // Then
        assertEquals(1, results.size());
        assertEquals(dto, results.get(0));
    }

    @Test
    @DisplayName("""
            findPopular | Returns empty list when TMDB returns null
            """)
    void findPopular_NullResponse_ShouldReturnEmptyList() {
        // Given
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(TmdbContentResponse.class)).thenReturn(null);

        // When
        List<ContentDto> results = seriesServiceImpl.findPopular();

        // Then
        assertEquals(0, results.size());
        verify(contentLikeService, never()).getLikesByContentIds(any(), any());
    }

    // ── findCast ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("""
            findCast | Returns sorted cast list limited to 10
            """)
    void findCast_ValidId_ShouldReturnSortedCast() {
        // Given
        Long seriesId = 1L;
        List<CastDto> cast = List.of(
                new CastDto(1L, "Actor A", "Role A", "/a.jpg", 2),
                new CastDto(2L, "Actor B", "Role B", "/b.jpg", 0),
                new CastDto(3L, "Actor C", "Role C", "/c.jpg", 1)
        );
        TmdbCreditsResponse credits = new TmdbCreditsResponse(cast);

        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class), any(Object[].class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(TmdbCreditsResponse.class)).thenReturn(credits);

        // When
        List<CastDto> result = seriesServiceImpl.findCast(seriesId);

        // Then
        assertEquals(3, result.size());
        assertEquals("Actor B", result.get(0).name());
        assertEquals("Actor C", result.get(1).name());
        assertEquals("Actor A", result.get(2).name());
    }

    @Test
    @DisplayName("""
            findCast | Throws EntityNotFoundException when TMDB returns null
            """)
    void findCast_NullResponse_ShouldThrow() {
        // Given
        Long seriesId = 999L;

        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class), any(Object[].class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(TmdbCreditsResponse.class)).thenReturn(null);

        // When / Then
        assertThrows(EntityNotFoundException.class,
                () -> seriesServiceImpl.findCast(seriesId));
    }

    // ── findSimilar ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("""
            findSimilar | Returns list of similar series with likes
            """)
    void findSimilar_ValidId_ShouldReturnList() {
        // Given
        Long seriesId = 1L;
        TmdbContentResult result = buildContentResult(2L);
        TmdbContentResponse response = new TmdbContentResponse(1, List.of(result), 1, 1);
        ContentDto dto = buildContentDto(2L);

        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class), any(Object[].class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(TmdbContentResponse.class)).thenReturn(response);
        when(contentLikeService.getLikesByContentIds(List.of(2L), Content.ContentType.SERIES))
                .thenReturn(Map.of(2L, noLikes()));
        when(seriesMapper.toDto(result, 0L, 0L)).thenReturn(dto);

        // When
        List<ContentDto> results = seriesServiceImpl.findSimilar(seriesId);

        // Then
        assertEquals(1, results.size());
        assertEquals(dto, results.get(0));
    }

    @Test
    @DisplayName("""
            findSimilar | Throws EntityNotFoundException when TMDB returns null
            """)
    void findSimilar_NullResponse_ShouldThrow() {
        // Given
        Long seriesId = 999L;

        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class), any(Object[].class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(TmdbContentResponse.class)).thenReturn(null);

        // When / Then
        assertThrows(EntityNotFoundException.class,
                () -> seriesServiceImpl.findSimilar(seriesId));
    }

    // ── findById ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("""
            findById | Returns SeriesDetailDto with isLiked when email provided
            """)
    void findById_WithEmail_ShouldReturnDtoWithIsLiked() {
        // Given
        Long seriesId = 1L;
        String email = "john@test.com";

        TmdbSeriesDetailResponse response = new TmdbSeriesDetailResponse(
                seriesId, "Test Series", "tagline", "overview", "Ended",
                "2020-01-01", 10, 2, 6.5, 7.0, 200,
                null, "/poster.jpg", "/backdrop.jpg", List.of(), List.of(), null
        );

        SeriesDetailDto dto = new SeriesDetailDto(
                seriesId, "Test Series", Content.ContentType.SERIES, "tagline",
                "overview", "Ended", "2020-01-01", 10, 2, 6.5, 7.0, 200,
                null, "/poster.jpg", "/backdrop.jpg", List.of(), List.of(),
                5L, 1L, true
        );

        when(contentLikeService.isLiked(email, seriesId, Content.ContentType.SERIES))
                .thenReturn(true);
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class), any(Object[].class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(TmdbSeriesDetailResponse.class)).thenReturn(response);
        when(contentLikeService.getLikes(seriesId, Content.ContentType.SERIES))
                .thenReturn(new ContentLikeResponseDto(5L, 1L));
        when(seriesMapper.toDetailDto(response, 5L, 1L, true)).thenReturn(dto);

        // When
        SeriesDetailDto result = seriesServiceImpl.findById(seriesId, email);

        // Then
        assertNotNull(result);
        assertEquals(dto, result);
        verify(contentLikeService, times(1)).isLiked(email, seriesId, Content.ContentType.SERIES);
    }

    @Test
    @DisplayName("""
            findById | Returns SeriesDetailDto with null isLiked when email is null
            """)
    void findById_WithoutEmail_ShouldReturnDtoWithNullIsLiked() {
        // Given
        Long seriesId = 1L;

        TmdbSeriesDetailResponse response = new TmdbSeriesDetailResponse(
                seriesId, "Test Series", "tagline", "overview", "Ended",
                "2020-01-01", 10, 2, 6.5, 7.0, 200,
                null, "/poster.jpg", "/backdrop.jpg", List.of(), List.of(), null
        );

        SeriesDetailDto dto = new SeriesDetailDto(
                seriesId, "Test Series", Content.ContentType.SERIES, "tagline",
                "overview", "Ended", "2020-01-01", 10, 2, 6.5, 7.0, 200,
                null, "/poster.jpg", "/backdrop.jpg", List.of(), List.of(),
                0L, 0L, false
        );

        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class), any(Object[].class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(TmdbSeriesDetailResponse.class)).thenReturn(response);
        when(contentLikeService.getLikes(seriesId, Content.ContentType.SERIES))
                .thenReturn(noLikes());
        when(seriesMapper.toDetailDto(response, 0L, 0L, null)).thenReturn(dto);

        // When
        SeriesDetailDto result = seriesServiceImpl.findById(seriesId, null);

        // Then
        assertNotNull(result);
        verify(contentLikeService, never()).isLiked(any(), anyLong(), any());
    }

    @Test
    @DisplayName("""
            findById | Throws EntityNotFoundException when TMDB returns null
            """)
    void findById_NullResponse_ShouldThrow() {
        // Given
        Long seriesId = 999L;

        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class), any(Object[].class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(TmdbSeriesDetailResponse.class)).thenReturn(null);

        // When / Then
        assertThrows(EntityNotFoundException.class,
                () -> seriesServiceImpl.findById(seriesId, null));
    }

    // ── findRecommendations ───────────────────────────────────────────────────

    @Test
    @DisplayName("""
            findRecommendations | Uses user genre ratings when email provided
            """)
    void findRecommendations_WithEmail_ShouldUseUserRatings() {
        // Given
        String email = "john@test.com";
        Map<Long, Double> userRatings = Map.of(18L, 8.0);

        TmdbContentResult result = buildContentResult(1L);
        TmdbContentResponse response = new TmdbContentResponse(1, List.of(result), 1, 1);
        ContentDto dto = buildContentDto(1L);

        when(favoriteGenreService.getUserGenreRatings(email)).thenReturn(userRatings);
        mockRestClientChainBoth();
        when(responseSpec.body(TmdbContentResponse.class)).thenReturn(response);
        when(scorer.calculateFinalScore(any(), eq(userRatings))).thenReturn(9.0);
        when(contentLikeService.getLikes(anyLong(), eq(Content.ContentType.SERIES)))
                .thenReturn(noLikes());
        when(seriesMapper.toDto(any(), eq(0L), eq(0L))).thenReturn(dto);

        // When
        List<ContentDto> results = seriesServiceImpl.findRecommendations(email);

        // Then
        assertNotNull(results);
        verify(favoriteGenreService, times(1)).getUserGenreRatings(email);
        verify(favoriteGenreService, never()).getRandomGenreIds(any());
    }

    @Test
    @DisplayName("""
            findRecommendations | Uses random genre ids when email is null
            """)
    void findRecommendations_WithoutEmail_ShouldUseRandomGenres() {
        // Given
        List<Long> randomGenreIds = List.of(18L);
        TmdbContentResult result = buildContentResult(1L);
        TmdbContentResponse response = new TmdbContentResponse(1, List.of(result), 1, 1);
        ContentDto dto = buildContentDto(1L);

        when(favoriteGenreService.getRandomGenreIds(GenreType.SERIES)).thenReturn(randomGenreIds);
        mockRestClientChainBoth();
        when(responseSpec.body(TmdbContentResponse.class)).thenReturn(response);
        when(scorer.calculateFinalScore(any(), eq(Map.of()))).thenReturn(7.0);
        when(contentLikeService.getLikes(anyLong(), eq(Content.ContentType.SERIES)))
                .thenReturn(noLikes());
        when(seriesMapper.toDto(any(), eq(0L), eq(0L))).thenReturn(dto);

        // When
        List<ContentDto> results = seriesServiceImpl.findRecommendations(null);

        // Then
        assertNotNull(results);
        verify(favoriteGenreService, never()).getUserGenreRatings(any());
        verify(favoriteGenreService, times(1)).getRandomGenreIds(GenreType.SERIES);
    }
}
