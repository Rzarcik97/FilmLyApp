package filmly.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import filmly.dto.genre.GenreDto;
import filmly.enums.GenreType;
import filmly.exception.EntityNotFoundException;
import filmly.service.GenreService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

public class GenreControllerTest extends BaseControllerTest {

    @MockitoBean
    private GenreService genreService;

    // ── getAllGenres ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("""
            GET /genres | Returns list of all genres
            """)
    void getAllGenres_ShouldReturnList() throws Exception {
        // Given
        List<GenreDto> genres = List.of(
                new GenreDto(28L, "Action", GenreType.MOVIE, "/images/genres/Action.png"),
                new GenreDto(18L, "Drama", GenreType.BOTH, "/images/genres/Drama.png")
        );

        when(genreService.getAllGenres()).thenReturn(genres);

        // When / Then
        mockMvc.perform(get("/genres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Action"))
                .andExpect(jsonPath("$[0].type").value("MOVIE"))
                .andExpect(jsonPath("$[1].name").value("Drama"))
                .andExpect(jsonPath("$[1].type").value("BOTH"));
    }

    @Test
    @DisplayName("""
            GET /genres | Returns empty list when no genres exist
            """)
    void getAllGenres_EmptyList_ShouldReturnEmptyArray() throws Exception {
        // Given
        when(genreService.getAllGenres()).thenReturn(List.of());

        // When / Then
        mockMvc.perform(get("/genres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // ── getGenreById ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("""
            GET /genres/{id} | Returns genre when exists
            """)
    void getGenreById_GenreExists_ShouldReturnGenre() throws Exception {
        // Given
        GenreDto genre = new GenreDto(28L, "Action", GenreType.MOVIE, "/images/genres/Action.png");

        when(genreService.getGenreById(28L)).thenReturn(genre);

        // When / Then
        mockMvc.perform(get("/genres/28"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Action"))
                .andExpect(jsonPath("$.type").value("MOVIE"))
                .andExpect(jsonPath("$.imagePath").value("/images/genres/Action.png"));
    }

    @Test
    @DisplayName("""
            GET /genres/{id} | Returns 404 when genre not found
            """)
    void getGenreById_GenreNotFound_ShouldReturn404() throws Exception {
        // Given
        when(genreService.getGenreById(999L))
                .thenThrow(new EntityNotFoundException("Genre", 999L));

        // When / Then
        mockMvc.perform(get("/genres/999"))
                .andExpect(status().isNotFound());
    }
}
