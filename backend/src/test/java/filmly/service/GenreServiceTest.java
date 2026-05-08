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
import filmly.dto.genre.GenreDto;
import filmly.dto.tmdb.TmdbGenreResponse;
import filmly.enums.GenreType;
import filmly.exception.EntityNotFoundException;
import filmly.mapper.GenreMapper;
import filmly.model.Genre;
import filmly.repository.GenreRepository;
import filmly.service.impl.GenreServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

@ExtendWith(MockitoExtension.class)
public class GenreServiceTest {

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private GenreMapper genreMapper;

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private RestClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @InjectMocks
    private GenreServiceImpl genreServiceImpl;

    // ── helpers ──────────────────────────────────────────────────────────────

    private void mockRestClientChain() {
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    private Genre buildGenre(Long id, String name, GenreType type) {
        Genre genre = new Genre();
        genre.setId(id);
        genre.setName(name);
        genre.setType(type);
        genre.setImagePath("/images/genres/" + name + ".png");
        return genre;
    }

    private GenreDto buildGenreDto(Long id, String name, GenreType type) {
        return new GenreDto(id, name, type, "/images/genres/" + name + ".png");
    }

    // ── getGenreById ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("""
            getGenreById | Returns GenreDto when genre exists
            """)
    void getGenreById_GenreExists_ShouldReturnDto() {
        // Given
        Long id = 28L;
        Genre genre = buildGenre(id, "Action", GenreType.MOVIE);
        GenreDto dto = buildGenreDto(id, "Action", GenreType.MOVIE);

        when(genreRepository.findById(id)).thenReturn(Optional.of(genre));
        when(genreMapper.toDto(genre)).thenReturn(dto);

        // When
        GenreDto result = genreServiceImpl.getGenreById(id);

        // Then
        assertNotNull(result);
        assertEquals(dto, result);
        verify(genreRepository, times(1)).findById(id);
        verify(genreMapper, times(1)).toDto(genre);
    }

    @Test
    @DisplayName("""
            getGenreById | Throws EntityNotFoundException when genre not found
            """)
    void getGenreById_GenreNotFound_ShouldThrow() {
        // Given
        Long id = 999L;

        when(genreRepository.findById(id)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(EntityNotFoundException.class,
                () -> genreServiceImpl.getGenreById(id));

        verify(genreRepository, times(1)).findById(id);
        verify(genreMapper, never()).toDto(any());
    }

    // ── getAllGenres ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("""
            getAllGenres | Returns list of GenreDto when genres exist
            """)
    void getAllGenres_GenresExist_ShouldReturnList() {
        // Given
        Genre action = buildGenre(28L, "Action", GenreType.MOVIE);
        Genre drama = buildGenre(18L, "Drama", GenreType.BOTH);

        GenreDto actionDto = buildGenreDto(28L, "Action", GenreType.MOVIE);
        GenreDto dramaDto = buildGenreDto(18L, "Drama", GenreType.BOTH);

        when(genreRepository.findAll()).thenReturn(List.of(action, drama));
        when(genreMapper.toDto(action)).thenReturn(actionDto);
        when(genreMapper.toDto(drama)).thenReturn(dramaDto);

        // When
        List<GenreDto> result = genreServiceImpl.getAllGenres();

        // Then
        assertEquals(2, result.size());
        assertEquals(actionDto, result.get(0));
        assertEquals(dramaDto, result.get(1));
        verify(genreRepository, times(1)).findAll();
        verify(genreMapper, times(2)).toDto(any());
    }

    @Test
    @DisplayName("""
            getAllGenres | Returns empty list when no genres exist
            """)
    void getAllGenres_NoGenres_ShouldReturnEmptyList() {
        // Given
        when(genreRepository.findAll()).thenReturn(List.of());

        // When
        List<GenreDto> result = genreServiceImpl.getAllGenres();

        // Then
        assertEquals(0, result.size());
        verify(genreRepository, times(1)).findAll();
        verify(genreMapper, never()).toDto(any());
    }

    // ── syncGenres ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("""
            syncGenres | Does nothing when genres already exist in repository
            """)
    void syncGenres_GenresAlreadyExist_ShouldSkip() {
        // Given
        when(genreRepository.count()).thenReturn(5L);

        // When
        genreServiceImpl.syncGenres();

        // Then
        verify(genreRepository, times(1)).count();
        verify(genreRepository, never()).saveAll(any());
        verify(restClient, never()).get();
    }

    @Test
    @DisplayName("""
            syncGenres | Fetches and saves genres when repository is empty
            """)
    void syncGenres_EmptyRepository_ShouldFetchAndSave() {
        // Given
        when(genreRepository.count()).thenReturn(0L);

        GenreDto actionDto = buildGenreDto(28L, "Action", GenreType.MOVIE);
        GenreDto dramaDto = buildGenreDto(18L, "Drama", GenreType.BOTH);

        TmdbGenreResponse movieResponse = new TmdbGenreResponse(List.of(actionDto));
        TmdbGenreResponse tvResponse = new TmdbGenreResponse(List.of(dramaDto));

        Genre actionGenre = buildGenre(28L, "Action", GenreType.MOVIE);
        Genre dramaGenre = buildGenre(18L, "Drama", GenreType.SERIES);

        mockRestClientChain();
        when(responseSpec.body(TmdbGenreResponse.class))
                .thenReturn(movieResponse)
                .thenReturn(tvResponse);

        when(genreMapper.toEntity(actionDto)).thenReturn(actionGenre);
        when(genreMapper.toEntity(dramaDto)).thenReturn(dramaGenre);

        // When
        genreServiceImpl.syncGenres();

        // Then
        verify(genreRepository, times(1)).count();
        verify(genreRepository, times(1)).saveAll(any());
    }

    @Test
    @DisplayName("""
            syncGenres | Sets genre type to BOTH when genre exists in both movie and tv lists
            """)
    void syncGenres_GenreInBothLists_ShouldSetTypeBoth() {
        // Given
        when(genreRepository.count()).thenReturn(0L);

        GenreDto sharedDto = buildGenreDto(18L, "Drama", GenreType.MOVIE);

        TmdbGenreResponse movieResponse = new TmdbGenreResponse(List.of(sharedDto));
        TmdbGenreResponse tvResponse = new TmdbGenreResponse(List.of(sharedDto));

        Genre movieGenre = buildGenre(18L, "Drama", GenreType.MOVIE);
        Genre tvGenre = buildGenre(18L, "Drama", GenreType.SERIES);

        mockRestClientChain();
        when(responseSpec.body(TmdbGenreResponse.class))
                .thenReturn(movieResponse)
                .thenReturn(tvResponse);

        when(genreMapper.toEntity(sharedDto))
                .thenReturn(movieGenre)
                .thenReturn(tvGenre);

        // When
        genreServiceImpl.syncGenres();

        // Then
        assertEquals(GenreType.BOTH, movieGenre.getType());
        verify(genreRepository, times(1)).saveAll(any());
    }

    @Test
    @DisplayName("""
            syncGenres | Sets image path based on genre name
            """)
    void syncGenres_ShouldSetImagePathFromGenreName() {
        // Given
        when(genreRepository.count()).thenReturn(0L);

        GenreDto actionDto = buildGenreDto(28L, "Action", GenreType.MOVIE);
        TmdbGenreResponse movieResponse = new TmdbGenreResponse(List.of(actionDto));
        TmdbGenreResponse tvResponse = new TmdbGenreResponse(List.of());

        Genre actionGenre = new Genre();
        actionGenre.setId(28L);
        actionGenre.setName("Action");

        mockRestClientChain();
        when(responseSpec.body(TmdbGenreResponse.class))
                .thenReturn(movieResponse)
                .thenReturn(tvResponse);
        when(genreMapper.toEntity(actionDto)).thenReturn(actionGenre);

        // When
        genreServiceImpl.syncGenres();

        // Then
        assertEquals("/images/genres/Action.png", actionGenre.getImagePath());
    }

    @Test
    @DisplayName("""
            syncGenres | Handles null response from TMDB gracefully
            """)
    void syncGenres_NullTmdbResponse_ShouldSaveEmpty() {
        // Given
        when(genreRepository.count()).thenReturn(0L);

        mockRestClientChain();
        when(responseSpec.body(TmdbGenreResponse.class)).thenReturn(null);

        // When
        genreServiceImpl.syncGenres();

        // Then
        verify(genreRepository, times(1)).saveAll(any());
    }
}
