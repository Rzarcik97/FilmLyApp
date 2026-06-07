package filmly.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import filmly.dto.content.ContentDto;
import filmly.dto.search.PagedResponse;
import filmly.model.Content;
import filmly.service.SearchService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

public class SearchControllerTest extends BaseControllerTest {

    @MockitoBean
    private SearchService searchService;

    private ContentDto buildContentDto(Long id) {
        return new ContentDto(id, Content.ContentType.MOVIE, "Test Movie " + id,
                "/poster.jpg", List.of(), "2024-01-01", 7.5, 100, 0L, 0L);
    }

    private PagedResponse<ContentDto> buildPagedResponse(List<ContentDto> results) {
        return new PagedResponse<>(results, 1, 5, results.size());
    }

    // ── search ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("""
            GET /search | Returns paged results for title search
            """)
    void search_WithTitle_ShouldReturnPagedResults() throws Exception {
        // Given
        PagedResponse<ContentDto> response = buildPagedResponse(
                List.of(buildContentDto(1L), buildContentDto(2L)));

        when(searchService.search(eq("Test"), isNull(), eq(1))).thenReturn(response);

        // When / Then
        mockMvc.perform(get("/search")
                        .param("title", "Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results.length()").value(2))
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.totalPages").value(5))
                .andExpect(jsonPath("$.totalResults").value(2));
    }

    @Test
    @DisplayName("""
            GET /search | Returns filtered results when type is provided
            """)
    void search_WithTypeFilter_ShouldReturnFilteredResults() throws Exception {
        // Given
        PagedResponse<ContentDto> response = buildPagedResponse(List.of(buildContentDto(1L)));

        when(searchService.search(eq("Test"), eq(Content.ContentType.MOVIE), eq(1)))
                .thenReturn(response);

        // When / Then
        mockMvc.perform(get("/search")
                        .param("title", "Test")
                        .param("type", "MOVIE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results.length()").value(1))
                .andExpect(jsonPath("$.results[0].type").value("MOVIE"));
    }

    @Test
    @DisplayName("""
            GET /search | Uses correct page number
            """)
    void search_WithPageParam_ShouldUsePage() throws Exception {
        // Given
        PagedResponse<ContentDto> response = buildPagedResponse(List.of(buildContentDto(1L)));

        when(searchService.search(eq("Test"), isNull(), eq(2))).thenReturn(response);

        // When / Then
        mockMvc.perform(get("/search")
                        .param("title", "Test")
                        .param("page", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(1));
    }

    @Test
    @DisplayName("""
            GET /search | Returns 400 when title is missing
            """)
    void search_MissingTitle_ShouldReturn400() throws Exception {
        mockMvc.perform(get("/search"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("""
            GET /search | Returns empty results when nothing found
            """)
    void search_NoResults_ShouldReturnEmptyList() throws Exception {
        // Given
        PagedResponse<ContentDto> response = buildPagedResponse(List.of());

        when(searchService.search(eq("NonExistent"), isNull(), eq(1))).thenReturn(response);

        // When / Then
        mockMvc.perform(get("/search")
                        .param("title", "NonExistent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results.length()").value(0));
    }

    // ── discover ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("""
            GET /search/discover | Returns paged results with filters
            """)
    void discover_WithFilters_ShouldReturnResults() throws Exception {
        // Given
        PagedResponse<ContentDto> response = buildPagedResponse(List.of(buildContentDto(1L)));

        when(searchService.discover(any())).thenReturn(response);

        // When / Then
        mockMvc.perform(get("/search/discover")
                        .param("type", "MOVIE")
                        .param("ratingMin", "7.0")
                        .param("ratingMax", "10.0")
                        .param("dateFrom", "2020-01-01")
                        .param("dateTo", "2026-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results.length()").value(1));
    }

    @Test
    @DisplayName("""
            GET /search/discover | Returns results with sort param
            """)
    void discover_WithSortBy_ShouldReturnResults() throws Exception {
        // Given
        PagedResponse<ContentDto> response = buildPagedResponse(List.of(buildContentDto(1L)));

        when(searchService.discover(any())).thenReturn(response);

        // When / Then
        mockMvc.perform(get("/search/discover")
                        .param("type", "MOVIE")
                        .param("sortBy", "POPULARITY_DESC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results.length()").value(1));
    }

    @Test
    @DisplayName("""
            GET /search/discover | Returns 400 when type is missing
            """)
    void discover_MissingType_ShouldReturn400() throws Exception {
        mockMvc.perform(get("/search/discover"))
                .andExpect(status().isBadRequest());
    }
}
