package filmly.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import filmly.dto.watchlist.WatchListRequestDto;
import filmly.dto.watchlist.WatchListResponseDto;
import filmly.exception.EntityNotFoundException;
import filmly.model.Content;
import filmly.service.WatchListService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

public class WatchListControllerTest extends BaseControllerTest {

    @MockitoBean
    private WatchListService watchListService;

    @Autowired
    private ObjectMapper objectMapper;

    private WatchListResponseDto buildResponseDto() {
        return new WatchListResponseDto(
                1L, 1630423L, Content.ContentType.MOVIE, "Test Movie",
                "/poster.jpg", List.of(), "2024-01-01", 7.5, 100,
                0L, 0L, null, LocalDateTime.now()
        );
    }

    // ── getWatchList ──────────────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "john@test.com")
    @DisplayName("""
            GET /users/watchlist | Returns watchlist for authenticated user
            """)
    void getWatchList_ShouldReturnList() throws Exception {
        // Given
        when(watchListService.getWatchList("john@test.com", true, Content.ContentType.MOVIE))
                .thenReturn(List.of(buildResponseDto()));

        // When / Then
        mockMvc.perform(get("/users/watchlist")
                        .param("type", "MOVIE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].contentId").value(1630423))
                .andExpect(jsonPath("$[0].title").value("Test Movie"));
    }

    @Test
    @WithMockUser(username = "john@test.com")
    @DisplayName("""
            GET /users/watchlist | Returns only unwatched when showWatched=false
            """)
    void getWatchList_ShowWatchedFalse_ShouldReturnUnwatched() throws Exception {
        // Given
        when(watchListService.getWatchList("john@test.com", false, Content.ContentType.MOVIE))
                .thenReturn(List.of(buildResponseDto()));

        // When / Then
        mockMvc.perform(get("/users/watchlist")
                        .param("type", "MOVIE")
                        .param("showWatched", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("""
            GET /users/watchlist | Returns 401 when not authenticated
            """)
    void getWatchList_NotAuthenticated_ShouldReturn401() throws Exception {
        mockMvc.perform(get("/users/watchlist")
                        .param("type", "MOVIE"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "john@test.com")
    @DisplayName("""
            GET /users/watchlist | Returns 400 when type is missing
            """)
    void getWatchList_MissingType_ShouldReturn400() throws Exception {
        mockMvc.perform(get("/users/watchlist"))
                .andExpect(status().isBadRequest());
    }

    // ── addToWatchList ────────────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "john@test.com")
    @DisplayName("""
            POST /users/watchlist | Returns created watchlist entry with 201
            """)
    void addToWatchList_ValidRequest_ShouldReturn201() throws Exception {
        // Given
        WatchListRequestDto request = new WatchListRequestDto(1630423L, Content.ContentType.MOVIE);

        when(watchListService.addToWatchList(eq("john@test.com"), any()))
                .thenReturn(buildResponseDto());

        // When / Then
        mockMvc.perform(post("/users/watchlist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.contentId").value(1630423))
                .andExpect(jsonPath("$.title").value("Test Movie"));
    }

    @Test
    @WithMockUser(username = "john@test.com")
    @DisplayName("""
            POST /users/watchlist | Returns 400 when request is invalid
            """)
    void addToWatchList_InvalidRequest_ShouldReturn400() throws Exception {
        // Given — null fields
        String invalidRequest = """
                {
                    "contentId": null,
                    "contentType": null
                }
                """;

        // When / Then
        mockMvc.perform(post("/users/watchlist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("""
            POST /users/watchlist | Returns 401 when not authenticated
            """)
    void addToWatchList_NotAuthenticated_ShouldReturn401() throws Exception {
        WatchListRequestDto request = new WatchListRequestDto(1630423L, Content.ContentType.MOVIE);

        mockMvc.perform(post("/users/watchlist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    // ── markAsWatched ─────────────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "john@test.com")
    @DisplayName("""
            PATCH /users/watchlist/watched | Returns updated entry after marking as watched
            """)
    void markAsWatched_ValidRequest_ShouldReturnUpdated() throws Exception {
        // Given
        WatchListRequestDto request = new WatchListRequestDto(1630423L, Content.ContentType.MOVIE);
        WatchListResponseDto watched = new WatchListResponseDto(
                1L, 1630423L, Content.ContentType.MOVIE, "Test Movie",
                "/poster.jpg", List.of(), "2024-01-01", 7.5, 100,
                0L, 0L, LocalDateTime.now(), LocalDateTime.now()
        );

        when(watchListService.markAsWatched(eq("john@test.com"), any())).thenReturn(watched);

        // When / Then
        mockMvc.perform(patch("/users/watchlist/watched")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.watchedAt").isNotEmpty());
    }

    @Test
    @WithMockUser(username = "john@test.com")
    @DisplayName("""
            PATCH /users/watchlist/watched | Returns 404 when content not in watchlist
            """)
    void markAsWatched_NotInWatchList_ShouldReturn404() throws Exception {
        // Given
        WatchListRequestDto request = new WatchListRequestDto(999L, Content.ContentType.MOVIE);

        when(watchListService.markAsWatched(eq("john@test.com"), any()))
                .thenThrow(new EntityNotFoundException("WatchList", 999L));

        // When / Then
        mockMvc.perform(patch("/users/watchlist/watched")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("""
            PATCH /users/watchlist/watched | Returns 401 when not authenticated
            """)
    void markAsWatched_NotAuthenticated_ShouldReturn401() throws Exception {
        WatchListRequestDto request = new WatchListRequestDto(1630423L, Content.ContentType.MOVIE);

        mockMvc.perform(patch("/users/watchlist/watched")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    // ── deleteFromWatchList ───────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "john@test.com")
    @DisplayName("""
            DELETE /users/watchlist/{contentId} | Returns 204 when deleted successfully
            """)
    void deleteFromWatchList_ShouldReturn204() throws Exception {
        // Given
        WatchListRequestDto request = new WatchListRequestDto(1630423L, Content.ContentType.MOVIE);
        doNothing().when(watchListService).deleteFromWatchList(eq("john@test.com"), any());

        // When / Then
        mockMvc.perform(delete("/users/watchlist/1630423")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("""
            DELETE /users/watchlist/{contentId} | Returns 401 when not authenticated
            """)
    void deleteFromWatchList_NotAuthenticated_ShouldReturn401() throws Exception {
        WatchListRequestDto request = new WatchListRequestDto(1630423L, Content.ContentType.MOVIE);

        mockMvc.perform(delete("/users/watchlist/1630423")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
}