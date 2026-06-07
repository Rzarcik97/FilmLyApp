package filmly.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import filmly.dto.content.ContentDto;
import filmly.dto.contentlikes.ContentLikeRequestDto;
import filmly.dto.contentlikes.ContentLikeResponseDto;
import filmly.model.Content;
import filmly.service.ContentLikeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

public class ContentLikeControllerTest extends BaseControllerTest {

    @MockitoBean
    private ContentLikeService contentLikeService;

    @Autowired
    private ObjectMapper objectMapper;

    // ── toggleLike ────────────────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "john@test.com")
    @DisplayName("""
            POST /likes | Returns like counts after toggling like
            """)
    void toggleLike_ValidRequest_ShouldReturnLikeCounts() throws Exception {
        // Given
        ContentLikeRequestDto request = new ContentLikeRequestDto(
                1L, Content.ContentType.MOVIE, true
        );

        ContentLikeResponseDto response = new ContentLikeResponseDto(5L, 1L);

        when(contentLikeService.toggleLike(eq("john@test.com"), any()))
                .thenReturn(response);

        // When / Then
        mockMvc.perform(post("/likes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.likes").value(5))
                .andExpect(jsonPath("$.dislikes").value(1));
    }

    @Test
    @WithMockUser(username = "john@test.com")
    @DisplayName("""
            POST /likes | Returns 400 when request is invalid
            """)
    void toggleLike_InvalidRequest_ShouldReturn400() throws Exception {
        // Given — missing required fields
        String invalidRequest = """
                {
                    "contentId": null,
                    "contentType": null,
                    "isLike": null
                }
                """;

        // When / Then
        mockMvc.perform(post("/likes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("""
            POST /likes | Returns 401 when not authenticated
            """)
    void toggleLike_NotAuthenticated_ShouldReturn401() throws Exception {
        // Given
        ContentLikeRequestDto request = new ContentLikeRequestDto(
                1L, Content.ContentType.MOVIE, true
        );

        // When / Then
        mockMvc.perform(post("/likes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    // ── getLikedContent ───────────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "john@test.com")
    @DisplayName("""
            GET /likes | Returns list of liked content
            """)
    void getLikedContent_ShouldReturnList() throws Exception {
        // Given
        List<ContentDto> content = List.of(
                new ContentDto(1L, Content.ContentType.MOVIE, "Test Movie",
                        "/poster.jpg", List.of(), "2024-01-01", 7.5, 100, 5L, 1L)
        );

        when(contentLikeService.getLikedContent(
                eq("john@test.com"), eq(Content.ContentType.MOVIE), eq(true)))
                .thenReturn(content);

        // When / Then
        mockMvc.perform(get("/likes")
                        .param("type", "MOVIE")
                        .param("liked", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].contentId").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Movie"));
    }

    @Test
    @WithMockUser(username = "john@test.com")
    @DisplayName("""
            GET /likes | Returns empty list when no liked content
            """)
    void getLikedContent_NoContent_ShouldReturnEmptyList() throws Exception {
        // Given
        when(contentLikeService.getLikedContent(
                eq("john@test.com"), eq(Content.ContentType.MOVIE), eq(true)))
                .thenReturn(List.of());

        // When / Then
        mockMvc.perform(get("/likes")
                        .param("type", "MOVIE")
                        .param("liked", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("""
            GET /likes | Returns 401 when not authenticated
            """)
    void getLikedContent_NotAuthenticated_ShouldReturn401() throws Exception {
        // When / Then
        mockMvc.perform(get("/likes")
                        .param("type", "MOVIE")
                        .param("liked", "true"))
                .andExpect(status().isUnauthorized());
    }
}