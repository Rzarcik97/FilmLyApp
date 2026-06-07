package filmly.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Optional;
import filmly.dto.tmdb.TmdbGenreDto;
import filmly.dto.tmdb.TmdbMovieDetailResponse;
import filmly.dto.tmdb.TmdbSeriesDetailResponse;
import filmly.exception.EntityNotFoundException;
import filmly.model.Content;
import filmly.model.Genre;
import filmly.repository.ContentRepository;
import filmly.repository.GenreRepository;
import filmly.service.impl.ContentServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

@ExtendWith(MockitoExtension.class)
public class ContentServiceTest {

    @Mock
    private ContentRepository contentRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private RestClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @InjectMocks
    private ContentServiceImpl contentServiceImpl;

    // ── helpers ──────────────────────────────────────────────────────────────

    private void mockRestClientChain() {
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class), any(Object[].class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    private TmdbMovieDetailResponse buildMovieResponse() {
        return new TmdbMovieDetailResponse(
                1L,
                "Test Movie",
                "tagline",
                "overview",
                "Released",
                "2024-01-01",
                120,
                7.5,
                8.0,
                100,
                "/poster.jpg",
                "/backdrop.jpg",
                List.of(),
                List.of(new TmdbGenreDto(28L, "Action")),
                null,
                null
        );
    }

    private TmdbSeriesDetailResponse buildSeriesResponse() {
        return new TmdbSeriesDetailResponse(
                2L,
                "Test Series",
                "tagline",
                "overview",
                "Ended",
                "2020-01-01",
                10,
                2,
                6.5,
                7.0,
                200,
                null,
                "/series_poster.jpg",
                "/series_backdrop.jpg",
                List.of(),
                List.of(new TmdbGenreDto(18L, "Drama")),
                null
        );
    }

    private Genre buildGenre(Long id, String name) {
        Genre genre = new Genre();
        genre.setId(id);
        genre.setName(name);
        return genre;
    }

    // ── getOrCreate ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("""
            getOrCreate | Returns existing content when found in repository
            """)
    void getOrCreate_ContentExists_ShouldReturnExisting() {
        // Given
        Long externalId = 1L;
        Content.ContentType type = Content.ContentType.MOVIE;

        Content existing = new Content();
        existing.setExternalId(externalId);
        existing.setType(type);
        existing.setTitle("Existing Movie");

        when(contentRepository.findByExternalIdAndType(externalId, type))
                .thenReturn(Optional.of(existing));

        // When
        Content result = contentServiceImpl.getOrCreate(externalId, type);

        // Then
        assertNotNull(result);
        assertEquals("Existing Movie", result.getTitle());
        verify(contentRepository, times(1)).findByExternalIdAndType(externalId, type);
        verify(contentRepository, never()).save(any());
        verify(restClient, never()).get();
    }

    @Test
    @DisplayName("""
            getOrCreate | Creates new movie content when not found in repository
            """)
    void getOrCreate_MovieNotFound_ShouldFetchAndSave() {
        // Given
        Long externalId = 1L;
        Content.ContentType type = Content.ContentType.MOVIE;

        TmdbMovieDetailResponse movie = buildMovieResponse();
        Genre actionGenre = buildGenre(28L, "Action");

        when(contentRepository.findByExternalIdAndType(externalId, type))
                .thenReturn(Optional.empty());
        mockRestClientChain();
        when(responseSpec.body(TmdbMovieDetailResponse.class)).thenReturn(movie);
        when(genreRepository.findAllByNameIn(List.of("Action")))
                .thenReturn(List.of(actionGenre));
        when(contentRepository.save(any(Content.class))).thenAnswer(i -> i.getArgument(0));

        // When
        Content result = contentServiceImpl.getOrCreate(externalId, type);

        // Then
        assertNotNull(result);
        assertEquals("Test Movie", result.getTitle());
        assertEquals("/poster.jpg", result.getPosterPath());
        assertEquals("2024-01-01", result.getReleaseDate());
        assertEquals(8.0, result.getVoteAverage());
        assertEquals(100, result.getVoteCount());
        assertEquals(List.of(actionGenre), result.getGenres());
        verify(contentRepository, times(1)).save(any(Content.class));
    }

    @Test
    @DisplayName("""
            getOrCreate | Creates new series content when not found in repository
            """)
    void getOrCreate_SeriesNotFound_ShouldFetchAndSave() {
        // Given
        Long externalId = 2L;
        Content.ContentType type = Content.ContentType.SERIES;

        TmdbSeriesDetailResponse series = buildSeriesResponse();
        Genre dramaGenre = buildGenre(18L, "Drama");

        when(contentRepository.findByExternalIdAndType(externalId, type))
                .thenReturn(Optional.empty());
        mockRestClientChain();
        when(responseSpec.body(TmdbSeriesDetailResponse.class)).thenReturn(series);
        when(genreRepository.findAllByNameIn(List.of("Drama")))
                .thenReturn(List.of(dramaGenre));
        when(contentRepository.save(any(Content.class))).thenAnswer(i -> i.getArgument(0));

        // When
        Content result = contentServiceImpl.getOrCreate(externalId, type);

        // Then
        assertNotNull(result);
        assertEquals("Test Series", result.getTitle());
        assertEquals("/series_poster.jpg", result.getPosterPath());
        assertEquals("2020-01-01", result.getReleaseDate());
        assertEquals(7.0, result.getVoteAverage());
        assertEquals(200, result.getVoteCount());
        assertEquals(List.of(dramaGenre), result.getGenres());
        verify(contentRepository, times(1)).save(any(Content.class));
    }

    // ── findMovieDetails ─────────────────────────────────────────────────────

    @Test
    @DisplayName("""
            findMovieDetails | Returns response when TMDB returns valid data
            """)
    void findMovieDetails_ValidId_ShouldReturnResponse() {
        // Given
        Long id = 1L;
        TmdbMovieDetailResponse movie = buildMovieResponse();

        mockRestClientChain();
        when(responseSpec.body(TmdbMovieDetailResponse.class)).thenReturn(movie);

        // When
        TmdbMovieDetailResponse result = contentServiceImpl.findMovieDetails(id);

        // Then
        assertNotNull(result);
        assertEquals("Test Movie", result.title());
    }

    @Test
    @DisplayName("""
            findMovieDetails | Throws EntityNotFoundException when TMDB returns null
            """)
    void findMovieDetails_NullResponse_ShouldThrow() {
        // Given
        Long id = 999L;

        mockRestClientChain();
        when(responseSpec.body(TmdbMovieDetailResponse.class)).thenReturn(null);

        // When / Then
        assertThrows(EntityNotFoundException.class,
                () -> contentServiceImpl.findMovieDetails(id));
    }

    // ── findSeriesDetails ────────────────────────────────────────────────────

    @Test
    @DisplayName("""
            findSeriesDetails | Returns response when TMDB returns valid data
            """)
    void findSeriesDetails_ValidId_ShouldReturnResponse() {
        // Given
        Long id = 2L;
        TmdbSeriesDetailResponse series = buildSeriesResponse();

        mockRestClientChain();
        when(responseSpec.body(TmdbSeriesDetailResponse.class)).thenReturn(series);

        // When
        TmdbSeriesDetailResponse result = contentServiceImpl.findSeriesDetails(id);

        // Then
        assertNotNull(result);
        assertEquals("Test Series", result.name());
    }

    @Test
    @DisplayName("""
            findSeriesDetails | Throws EntityNotFoundException when TMDB returns null
            """)
    void findSeriesDetails_NullResponse_ShouldThrow() {
        // Given
        Long id = 999L;

        mockRestClientChain();
        when(responseSpec.body(TmdbSeriesDetailResponse.class)).thenReturn(null);

        // When / Then
        assertThrows(EntityNotFoundException.class,
                () -> contentServiceImpl.findSeriesDetails(id));
    }
}