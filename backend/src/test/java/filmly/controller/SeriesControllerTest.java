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
import filmly.dto.content.SeriesDetailDto;
import filmly.exception.EntityNotFoundException;
import filmly.model.Content;
import filmly.service.TmdbContentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

public class SeriesControllerTest extends BaseControllerTest {

    @MockitoBean
    private TmdbContentService<ContentDto, SeriesDetailDto> seriesService;

    private ContentDto buildContentDto(Long id) {
        return new ContentDto(id, Content.ContentType.SERIES, "Test Series " + id,
                "/poster.jpg", List.of(), "2024-01-01", 7.5, 100, 0L, 0L);
    }

    // ── findPopular ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("""
            GET /series/popular | Returns list of popular series
            """)
    void getPopularSeries_ShouldReturnList() throws Exception {
        // Given
        when(seriesService.findPopular()).thenReturn(List.of(buildContentDto(1L)));

        // When / Then
        mockMvc.perform(get("/series/popular"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].contentId").value(1));
    }

    // ── findTrending ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("""
            GET /series/trending | Returns list of trending series
            """)
    void getTrendingSeries_ShouldReturnList() throws Exception {
        // Given
        when(seriesService.findTrending()).thenReturn(List.of(buildContentDto(2L)));

        // When / Then
        mockMvc.perform(get("/series/trending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].contentId").value(2));
    }

    // ── findRecent ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("""
            GET /series/recent | Returns list of recent series
            """)
    void getRecentSeries_ShouldReturnList() throws Exception {
        // Given
        when(seriesService.findRecent()).thenReturn(List.of(buildContentDto(3L)));

        // When / Then
        mockMvc.perform(get("/series/recent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].contentId").value(3));
    }

    // ── findById ──────────────────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "john@test.com")
    @DisplayName("""
            GET /series/{id} | Returns SeriesDetailDto with isLiked when authenticated
            """)
    void getSeriesById_Authenticated_ShouldReturnDto() throws Exception {
        // Given
        SeriesDetailDto dto = new SeriesDetailDto(
                1L, "Test Series", Content.ContentType.SERIES, "tagline",
                "overview", "Ended", "2020-01-01", 10, 2, 6.5, 7.0, 200,
                null, "/poster.jpg", "/backdrop.jpg", List.of(), List.of(),
                5L, 1L, true
        );

        when(seriesService.findById(eq(1L), eq("john@test.com"))).thenReturn(dto);

        // When / Then
        mockMvc.perform(get("/series/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Series"))
                .andExpect(jsonPath("$.isLiked").value(true));
    }

    @Test
    @DisplayName("""
            GET /series/{id} | Returns SeriesDetailDto with null isLiked when anonymous
            """)
    void getSeriesById_Anonymous_ShouldReturnDtoWithNullIsLiked() throws Exception {
        // Given
        SeriesDetailDto dto = new SeriesDetailDto(
                1L, "Test Series", Content.ContentType.SERIES, "tagline",
                "overview", "Ended", "2020-01-01", 10, 2, 6.5, 7.0, 200,
                null, "/poster.jpg", "/backdrop.jpg", List.of(), List.of(),
                0L, 0L, false
        );

        when(seriesService.findById(eq(1L), isNull())).thenReturn(dto);

        // When / Then
        mockMvc.perform(get("/series/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("""
            GET /series/{id} | Returns 404 when series not found
            """)
    void getSeriesById_NotFound_ShouldReturn404() throws Exception {
        // Given
        when(seriesService.findById(eq(999L), any()))
                .thenThrow(new EntityNotFoundException("Series", 999L));

        // When / Then
        mockMvc.perform(get("/series/999"))
                .andExpect(status().isNotFound());
    }

    // ── findSimilar ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("""
            GET /series/{id}/similar | Returns similar series list
            """)
    void getSeriesSimilar_ShouldReturnList() throws Exception {
        // Given
        when(seriesService.findSimilar(1L))
                .thenReturn(List.of(buildContentDto(2L), buildContentDto(3L)));

        // When / Then
        mockMvc.perform(get("/series/1/similar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("""
            GET /series/{id}/similar | Returns 404 when series not found
            """)
    void getSeriesSimilar_NotFound_ShouldReturn404() throws Exception {
        // Given
        when(seriesService.findSimilar(999L))
                .thenThrow(new EntityNotFoundException("Series", 999L));

        // When / Then
        mockMvc.perform(get("/series/999/similar"))
                .andExpect(status().isNotFound());
    }

    // ── findCast ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("""
            GET /series/{id}/cast | Returns cast list
            """)
    void getSeriesCast_ShouldReturnCastList() throws Exception {
        // Given
        List<CastDto> cast = List.of(
                new CastDto(1L, "Actor One", "Role One", "/one.jpg", 0),
                new CastDto(2L, "Actor Two", "Role Two", "/two.jpg", 1)
        );

        when(seriesService.findCast(1L)).thenReturn(cast);

        // When / Then
        mockMvc.perform(get("/series/1/cast"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Actor One"));
    }

    @Test
    @DisplayName("""
            GET /series/{id}/cast | Returns 404 when series not found
            """)
    void getSeriesCast_NotFound_ShouldReturn404() throws Exception {
        // Given
        when(seriesService.findCast(999L))
                .thenThrow(new EntityNotFoundException("Series", 999L));

        // When / Then
        mockMvc.perform(get("/series/999/cast"))
                .andExpect(status().isNotFound());
    }

    // ── findRecommendations ───────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "john@test.com")
    @DisplayName("""
            GET /series/recommendations | Returns personalized recommendations when authenticated
            """)
    void getRecommendations_Authenticated_ShouldReturnList() throws Exception {
        // Given
        when(seriesService.findRecommendations("john@test.com"))
                .thenReturn(List.of(buildContentDto(1L), buildContentDto(2L)));

        // When / Then
        mockMvc.perform(get("/series/recommendations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("""
            GET /series/recommendations | Returns recommendations for anonymous user
            """)
    void getRecommendations_Anonymous_ShouldReturnList() throws Exception {
        // Given
        when(seriesService.findRecommendations(null))
                .thenReturn(List.of(buildContentDto(1L)));

        // When / Then
        mockMvc.perform(get("/series/recommendations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}
