package filmly.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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
import filmly.dto.contentrating.ContentRatingRequestDto;
import filmly.dto.contentrating.ContentRatingResponseDto;
import filmly.dto.contentrating.ContentRatingUpdateRequestDto;
import filmly.exception.EntityNotFoundException;
import filmly.model.Content;
import filmly.service.ContentRatingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

public class RatingControllerTest extends BaseControllerTest {

    @MockitoBean
    private ContentRatingService contentRatingService;

    @Autowired
    private ObjectMapper objectMapper;

    private ContentRatingResponseDto buildResponseDto() {
        return new ContentRatingResponseDto(
                1L, 1630423L, Content.ContentType.MOVIE, 8.0f,
                "Great movie!", "john_doe", "/avatar.jpg", LocalDateTime.now()
        );
    }

    // ── getByContentId ────────────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "john@test.com")
    @DisplayName("""
            GET /user/rating | Returns latest ratings for content
            """)
    void getByContentId_ShouldReturnRatingList() throws Exception {
        // Given
        List<ContentRatingResponseDto> ratings = List.of(buildResponseDto());

        when(contentRatingService.getByContentId(1630423L, Content.ContentType.MOVIE))
                .thenReturn(ratings);

        // When / Then
        mockMvc.perform(get("/user/rating")
                        .param("contentId", "1630423")
                        .param("contentType", "MOVIE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].rating").value(8.0))
                .andExpect(jsonPath("$[0].review").value("Great movie!"))
                .andExpect(jsonPath("$[0].username").value("john_doe"));
    }

    @Test
    @WithMockUser(username = "john@test.com")
    @DisplayName("""
            GET /user/rating | Returns empty list when no ratings exist
            """)
    void getByContentId_NoRatings_ShouldReturnEmptyList() throws Exception {
        // Given
        when(contentRatingService.getByContentId(999L, Content.ContentType.MOVIE))
                .thenReturn(List.of());

        // When / Then
        mockMvc.perform(get("/user/rating")
                        .param("contentId", "999")
                        .param("contentType", "MOVIE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // ── addRating ─────────────────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "john@test.com")
    @DisplayName("""
            POST /user/rating | Returns created rating when valid request
            """)
    void addRating_ValidRequest_ShouldReturnCreatedRating() throws Exception {
        // Given
        ContentRatingRequestDto request = new ContentRatingRequestDto(
                1630423L, Content.ContentType.MOVIE, 8.0f, "Great movie!"
        );

        when(contentRatingService.addRating(eq("john@test.com"), any()))
                .thenReturn(buildResponseDto());

        // When / Then
        mockMvc.perform(post("/user/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(8.0))
                .andExpect(jsonPath("$.review").value("Great movie!"));
    }

    @Test
    @WithMockUser(username = "john@test.com")
    @DisplayName("""
            POST /user/rating | Returns 400 when rating is out of range
            """)
    void addRating_InvalidRating_ShouldReturn400() throws Exception {
        // Given — rating above max
        String invalidRequest = """
                {
                    "contentId": 1630423,
                    "contentType": "MOVIE",
                    "rating": 15.0,
                    "review": "Great movie!"
                }
                """;

        // When / Then
        mockMvc.perform(post("/user/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("""
            POST /user/rating | Returns 401 when not authenticated
            """)
    void addRating_NotAuthenticated_ShouldReturn401() throws Exception {
        // Given
        ContentRatingRequestDto request = new ContentRatingRequestDto(
                1630423L, Content.ContentType.MOVIE, 8.0f, "Great movie!"
        );

        // When / Then
        mockMvc.perform(post("/user/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    // ── updateRating ──────────────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "john@test.com")
    @DisplayName("""
            PATCH /user/rating | Returns updated rating
            """)
    void updateRating_ValidRequest_ShouldReturnUpdated() throws Exception {
        // Given
        ContentRatingUpdateRequestDto request = new ContentRatingUpdateRequestDto(
                1630423L, Content.ContentType.MOVIE, 9.0f, "Even better!"
        );

        ContentRatingResponseDto updated = new ContentRatingResponseDto(
                1L, 1630423L, Content.ContentType.MOVIE, 9.0f,
                "Even better!", "john_doe", "/avatar.jpg", LocalDateTime.now()
        );

        when(contentRatingService.updateRating(eq("john@test.com"), any()))
                .thenReturn(updated);

        // When / Then
        mockMvc.perform(patch("/user/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(9.0))
                .andExpect(jsonPath("$.review").value("Even better!"));
    }

    @Test
    @DisplayName("""
            PATCH /user/rating | Returns 401 when not authenticated
            """)
    void updateRating_NotAuthenticated_ShouldReturn401() throws Exception {
        ContentRatingUpdateRequestDto request = new ContentRatingUpdateRequestDto(
                1630423L, Content.ContentType.MOVIE, 9.0f, "Even better!"
        );

        mockMvc.perform(patch("/user/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    // ── deleteRating ──────────────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "john@test.com")
    @DisplayName("""
            DELETE /user/rating/{id} | Returns 204 when rating deleted
            """)
    void deleteRating_ShouldReturn204() throws Exception {
        // Given
        doNothing().when(contentRatingService).deleteRating("john@test.com", 1L);

        // When / Then
        mockMvc.perform(delete("/user/rating/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "john@test.com")
    @DisplayName("""
            DELETE /user/rating/{id} | Returns 404 when rating not found
            """)
    void deleteRating_NotFound_ShouldReturn404() throws Exception {
        // Given
        doThrow(new EntityNotFoundException("Rating", 999L))
                .when(contentRatingService).deleteRating("john@test.com", 999L);

        // When / Then
        mockMvc.perform(delete("/user/rating/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("""
            DELETE /user/rating/{id} | Returns 401 when not authenticated
            """)
    void deleteRating_NotAuthenticated_ShouldReturn401() throws Exception {
        mockMvc.perform(delete("/user/rating/1"))
                .andExpect(status().isUnauthorized());
    }
}
