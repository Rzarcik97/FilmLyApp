package filmly.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import filmly.dto.content.CastDto;
import filmly.dto.content.ContentDto;
import filmly.dto.content.MovieDetailDto;
import filmly.exception.EntityNotFoundException;
import filmly.model.Content;
import filmly.service.MovieService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

public class MoviesControllerTest extends BaseControllerTest {

    @MockitoBean
    private MovieService movieService;

    private ContentDto buildContentDto(Long id) {
        return new ContentDto(id, Content.ContentType.MOVIE, "Test Movie " + id,
                "/poster.jpg", List.of(), "2024-01-01", 7.5, 100, 0L, 0L);
    }

    // ── findPopular ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("""
            GET /movies/popular | Returns list of popular movies
            """)
    void getPopularMovies_ShouldReturnList() throws Exception {
        // Given
        when(movieService.findPopular()).thenReturn(List.of(buildContentDto(1L)));

        // When / Then
        mockMvc.perform(get("/movies/popular"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].contentId").value(1));
    }

    // ── findTrending ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("""
            GET /movies/trending | Returns list of trending movies
            """)
    void getTrendingMovies_ShouldReturnList() throws Exception {
        // Given
        when(movieService.findTrending()).thenReturn(List.of(buildContentDto(2L)));

        // When / Then
        mockMvc.perform(get("/movies/trending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].contentId").value(2));
    }

    // ── findRecent ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("""
            GET /movies/recent | Returns list of recent movies
            """)
    void getRecentMovies_ShouldReturnList() throws Exception {
        // Given
        when(movieService.findRecent()).thenReturn(List.of(buildContentDto(3L)));

        // When / Then
        mockMvc.perform(get("/movies/recent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].contentId").value(3));
    }

    // ── findUpcoming ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("""
            GET /movies/upcoming | Returns list of upcoming movies
            """)
    void getUpcomingMovies_ShouldReturnList() throws Exception {
        // Given
        when(movieService.findUpcoming()).thenReturn(List.of(buildContentDto(4L)));

        // When / Then
        mockMvc.perform(get("/movies/upcoming"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].contentId").value(4));
    }

    // ── findById ──────────────────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "john@test.com")
    @DisplayName("""
            GET /movies/{id} | Returns MovieDetailDto with isLiked when authenticated
            """)
    void getMovieById_Authenticated_ShouldReturnDtoWithIsLiked() throws Exception {
        // Given
        MovieDetailDto dto = new MovieDetailDto(
                1L, "Test Movie", Content.ContentType.MOVIE, "tagline",
                "overview", "Released", "2024-01-01", 120, 7.5, 8.0, 100,
                null, "/poster.jpg", "/backdrop.jpg", List.of(), List.of(),
                5L, 1L, true
        );

        when(movieService.findById(eq(1L), eq("john@test.com"))).thenReturn(dto);

        // When / Then
        mockMvc.perform(get("/movies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Movie"))
                .andExpect(jsonPath("$.isLiked").value(true));
    }

    @Test
    @DisplayName("""
            GET /movies/{id} | Returns MovieDetailDto with null isLiked when anonymous
            """)
    void getMovieById_Anonymous_ShouldReturnDtoWithNullIsLiked() throws Exception {
        // Given
        MovieDetailDto dto = new MovieDetailDto(
                1L, "Test Movie", Content.ContentType.MOVIE, "tagline",
                "overview", "Released", "2024-01-01", 120, 7.5, 8.0, 100,
                null, "/poster.jpg", "/backdrop.jpg", List.of(), List.of(),
                0L, 0L, null
        );

        when(movieService.findById(eq(1L), isNull())).thenReturn(dto);

        // When / Then
        mockMvc.perform(get("/movies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isLiked").isEmpty());
    }

    @Test
    @DisplayName("""
            GET /movies/{id} | Returns 404 when movie not found
            """)
    void getMovieById_NotFound_ShouldReturn404() throws Exception {
        // Given
        when(movieService.findById(eq(999L), any()))
                .thenThrow(new EntityNotFoundException("Movie", 999L));

        // When / Then
        mockMvc.perform(get("/movies/999"))
                .andExpect(status().isNotFound());
    }

    // ── findCast ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("""
            GET /movies/{id}/cast | Returns cast list
            """)
    void getMovieCast_ShouldReturnCastList() throws Exception {
        // Given
        List<CastDto> cast = List.of(
                new CastDto(1L, "Actor One", "Role One", "/one.jpg", 0),
                new CastDto(2L, "Actor Two", "Role Two", "/two.jpg", 1)
        );

        when(movieService.findCast(1L)).thenReturn(cast);

        // When / Then
        mockMvc.perform(get("/movies/1/cast"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Actor One"))
                .andExpect(jsonPath("$[1].name").value("Actor Two"));
    }

    @Test
    @DisplayName("""
            GET /movies/{id}/cast | Returns 404 when movie not found
            """)
    void getMovieCast_NotFound_ShouldReturn404() throws Exception {
        // Given
        when(movieService.findCast(999L))
                .thenThrow(new EntityNotFoundException("Movie", 999L));

        // When / Then
        mockMvc.perform(get("/movies/999/cast"))
                .andExpect(status().isNotFound());
    }

    // ── findSimilar ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("""
            GET /movies/{id}/similar | Returns similar movies list
            """)
    void getMovieSimilar_ShouldReturnList() throws Exception {
        // Given
        when(movieService.findSimilar(1L))
                .thenReturn(List.of(buildContentDto(2L), buildContentDto(3L)));

        // When / Then
        mockMvc.perform(get("/movies/1/similar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // ── findRecommendations ───────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "john@test.com")
    @DisplayName("""
            GET /movies/recommendations | Returns personalized recommendations when authenticated
            """)
    void getRecommendations_Authenticated_ShouldReturnList() throws Exception {
        // Given
        when(movieService.findRecommendations("john@test.com"))
                .thenReturn(List.of(buildContentDto(1L), buildContentDto(2L)));

        // When / Then
        mockMvc.perform(get("/movies/recommendations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("""
            GET /movies/recommendations | Returns recommendations for anonymous user
            """)
    void getRecommendations_Anonymous_ShouldReturnList() throws Exception {
        // Given
        when(movieService.findRecommendations(null))
                .thenReturn(List.of(buildContentDto(1L)));

        // When / Then
        mockMvc.perform(get("/movies/recommendations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}
