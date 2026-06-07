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

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import filmly.dto.favoritegenres.FavoriteGenreDto;
import filmly.dto.favoritegenres.FavoriteGenreResponseDto;
import filmly.enums.GenreType;
import filmly.service.FavoriteGenreService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

public class FavoriteGenreControllerTest extends BaseControllerTest {

    @MockitoBean
    private FavoriteGenreService favoriteGenreService;

    @Autowired
    private ObjectMapper objectMapper;

    private FavoriteGenreResponseDto buildResponseDto() {
        return new FavoriteGenreResponseDto(
                1L, "Action", "/images/genres/Action.png", GenreType.MOVIE, 8.0f
        );
    }

    // ── getSortedFavoriteGenres ───────────────────────────────────────────────

    @Test
    @WithMockUser(username = "john@test.com")
    @DisplayName("""
            GET /users/favorite-genres/sorted | Returns sorted genres for authenticated user
            """)
    void getSortedFavoriteGenres_ShouldReturnSortedList() throws Exception {
        // Given
        List<FavoriteGenreResponseDto> genres = List.of(
                new FavoriteGenreResponseDto(1L, "Action", "/images/genres/Action.png",
                        GenreType.MOVIE, 9.0f),
                new FavoriteGenreResponseDto(2L, "Drama", "/images/genres/Drama.png",
                        GenreType.BOTH, 7.0f)
        );

        when(favoriteGenreService.getSortedFavoriteGenreByUserId("john@test.com"))
                .thenReturn(genres);

        // When / Then
        mockMvc.perform(get("/users/favorite-genres/sorted"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].genreName").value("Action"))
                .andExpect(jsonPath("$[0].rating").value(9.0))
                .andExpect(jsonPath("$[1].genreName").value("Drama"))
                .andExpect(jsonPath("$[1].rating").value(7.0));
    }

    @Test
    @DisplayName("""
            GET /users/favorite-genres/sorted | Returns 401 when not authenticated
            """)
    void getSortedFavoriteGenres_NotAuthenticated_ShouldReturn401() throws Exception {
        mockMvc.perform(get("/users/favorite-genres/sorted"))
                .andExpect(status().isUnauthorized());
    }

    // ── getAllFavoriteGenres ───────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "john@test.com")
    @DisplayName("""
            GET /users/favorite-genres | Returns all rated genres for authenticated user
            """)
    void getAllFavoriteGenres_ShouldReturnList() throws Exception {
        // Given
        List<FavoriteGenreResponseDto> genres = List.of(buildResponseDto());

        when(favoriteGenreService.getAllByUserId("john@test.com")).thenReturn(genres);

        // When / Then
        mockMvc.perform(get("/users/favorite-genres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].genreName").value("Action"))
                .andExpect(jsonPath("$[0].rating").value(8.0));
    }

    @Test
    @DisplayName("""
            GET /users/favorite-genres | Returns 401 when not authenticated
            """)
    void getAllFavoriteGenres_NotAuthenticated_ShouldReturn401() throws Exception {
        mockMvc.perform(get("/users/favorite-genres"))
                .andExpect(status().isUnauthorized());
    }

    // ── createFavoriteGenre ───────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "john@test.com")
    @DisplayName("""
            POST /users/favorite-genres | Returns created genre with 201
            """)
    void createFavoriteGenre_ValidRequest_ShouldReturn201() throws Exception {
        // Given
        FavoriteGenreDto request = new FavoriteGenreDto("Action", 8.0f);

        when(favoriteGenreService.createFavoriteGenre(eq("john@test.com"), any()))
                .thenReturn(buildResponseDto());

        // When / Then
        mockMvc.perform(post("/users/favorite-genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.genreName").value("Action"))
                .andExpect(jsonPath("$.rating").value(8.0));
    }

    @Test
    @WithMockUser(username = "john@test.com")
    @DisplayName("""
            POST /users/favorite-genres | Returns 400 when request is invalid
            """)
    void createFavoriteGenre_InvalidRequest_ShouldReturn400() throws Exception {
        // Given — rating out of range
        String invalidRequest = """
                {
                    "genreName": "",
                    "rating": 15.0
                }
                """;

        // When / Then
        mockMvc.perform(post("/users/favorite-genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("""
            POST /users/favorite-genres | Returns 401 when not authenticated
            """)
    void createFavoriteGenre_NotAuthenticated_ShouldReturn401() throws Exception {
        FavoriteGenreDto request = new FavoriteGenreDto("Action", 8.0f);

        mockMvc.perform(post("/users/favorite-genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    // ── updateFavoriteGenre ───────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "john@test.com")
    @DisplayName("""
            PATCH /users/favorite-genres | Returns updated genre
            """)
    void updateFavoriteGenre_ValidRequest_ShouldReturnUpdated() throws Exception {
        // Given
        FavoriteGenreDto request = new FavoriteGenreDto("Action", 9.0f);
        FavoriteGenreResponseDto updated = new FavoriteGenreResponseDto(
                1L, "Action", "/images/genres/Action.png", GenreType.MOVIE, 9.0f
        );

        when(favoriteGenreService.updateFavoriteGenre(eq("john@test.com"), any()))
                .thenReturn(updated);

        // When / Then
        mockMvc.perform(patch("/users/favorite-genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.genreName").value("Action"))
                .andExpect(jsonPath("$.rating").value(9.0));
    }

    @Test
    @DisplayName("""
            PATCH /users/favorite-genres | Returns 401 when not authenticated
            """)
    void updateFavoriteGenre_NotAuthenticated_ShouldReturn401() throws Exception {
        FavoriteGenreDto request = new FavoriteGenreDto("Action", 9.0f);

        mockMvc.perform(patch("/users/favorite-genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    // ── deleteFavoriteGenre ───────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "john@test.com")
    @DisplayName("""
            DELETE /users/favorite-genres | Returns 204 when genre deleted
            """)
    void deleteFavoriteGenre_ShouldReturn204() throws Exception {
        // Given
        doNothing().when(favoriteGenreService)
                .deleteFavoriteGenre("john@test.com", "Action");

        // When / Then
        mockMvc.perform(delete("/users/favorite-genres")
                        .param("genreName", "Action"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("""
            DELETE /users/favorite-genres | Returns 401 when not authenticated
            """)
    void deleteFavoriteGenre_NotAuthenticated_ShouldReturn401() throws Exception {
        mockMvc.perform(delete("/users/favorite-genres")
                        .param("genreName", "Action"))
                .andExpect(status().isUnauthorized());
    }
}