package filmly.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.ObjectMapper;
import filmly.dto.user.UserPatchRequestDto;
import filmly.dto.user.UserResponseDto;
import filmly.exception.EntityNotFoundException;
import filmly.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

public class UserControllerTest extends BaseControllerTest {

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserResponseDto buildUserResponse() {
        return new UserResponseDto(
                1L, "john_doe", "John", "Doe",
                "john@test.com", "/avatar.jpg", LocalDateTime.now()
        );
    }

    // ── getMyProfile ──────────────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "john@test.com")
    @DisplayName("""
            GET /users/me | Returns profile of authenticated user
            """)
    void getMyProfile_ShouldReturnUserDto() throws Exception {
        // Given
        when(userService.getMyProfile("john@test.com")).thenReturn(buildUserResponse());

        // When / Then
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.username").value("john_doe"))
                .andExpect(jsonPath("$.email").value("john@test.com"));
    }

    @Test
    @DisplayName("""
            GET /users/me | Returns 401 when not authenticated
            """)
    void getMyProfile_NotAuthenticated_ShouldReturn401() throws Exception {
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isUnauthorized());
    }

    // ── updateMyProfile ───────────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "john@test.com")
    @DisplayName("""
            PATCH /users/me | Returns updated profile
            """)
    void updateMyProfile_ValidRequest_ShouldReturnUpdated() throws Exception {
        // Given
        UserPatchRequestDto request = new UserPatchRequestDto(
                "john_doe", "John", "Doe", "/avatar.jpg"
        );

        when(userService.updateMyProfile(eq("john@test.com"), any()))
                .thenReturn(buildUserResponse());

        // When / Then
        mockMvc.perform(patch("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john_doe"));
    }

    @Test
    @WithMockUser(username = "john@test.com")
    @DisplayName("""
            PATCH /users/me | Returns 400 when request is invalid
            """)
    void updateMyProfile_InvalidRequest_ShouldReturn400() throws Exception {
        // Given — blank fields
        String invalidRequest = """
                {
                    "username": "",
                    "firstName": "",
                    "lastName": "",
                    "avatarUrl": ""
                }
                """;

        // When / Then
        mockMvc.perform(patch("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("""
            PATCH /users/me | Returns 401 when not authenticated
            """)
    void updateMyProfile_NotAuthenticated_ShouldReturn401() throws Exception {
        UserPatchRequestDto request = new UserPatchRequestDto(
                "john_doe", "John", "Doe", "/avatar.jpg"
        );

        mockMvc.perform(patch("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    // ── changeEmail ───────────────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "john@test.com")
    @DisplayName("""
            PATCH /users/me/change-email | Returns verification code
            """)
    void changeEmail_ValidRequest_ShouldReturnCode() throws Exception {
        // Given
        when(userService.changeEmail("john@test.com", "new@test.com", "password123"))
                .thenReturn("123456");

        // When / Then
        mockMvc.perform(patch("/users/me/change-email")
                        .param("newEmail", "new@test.com")
                        .param("currentPassword", "password123"))
                .andExpect(status().isOk())
                .andExpect(content().string("123456"));
    }

    @Test
    @WithMockUser(username = "john@test.com")
    @DisplayName("""
            PATCH /users/me/change-email | Returns 400 when email is invalid
            """)
    void changeEmail_InvalidEmail_ShouldReturn400() throws Exception {
        mockMvc.perform(patch("/users/me/change-email")
                        .param("newEmail", "not-an-email")
                        .param("currentPassword", "password123"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("""
            PATCH /users/me/change-email | Returns 401 when not authenticated
            """)
    void changeEmail_NotAuthenticated_ShouldReturn401() throws Exception {
        mockMvc.perform(patch("/users/me/change-email")
                        .param("newEmail", "new@test.com")
                        .param("currentPassword", "password123"))
                .andExpect(status().isUnauthorized());
    }

    // ── changePassword ────────────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "john@test.com")
    @DisplayName("""
            PATCH /users/me/change-password | Returns verification code
            """)
    void changePassword_ValidRequest_ShouldReturnCode() throws Exception {
        // Given
        when(userService.changePassword("john@test.com", "oldPass", "newPass123"))
                .thenReturn("654321");

        // When / Then
        mockMvc.perform(patch("/users/me/change-password")
                        .param("oldPassword", "oldPass")
                        .param("newPassword", "newPass123"))
                .andExpect(status().isOk())
                .andExpect(content().string("654321"));
    }

    @Test
    @WithMockUser(username = "john@test.com")
    @DisplayName("""
            PATCH /users/me/change-password | Returns 400 when new password is too short
            """)
    void changePassword_TooShortPassword_ShouldReturn400() throws Exception {
        mockMvc.perform(patch("/users/me/change-password")
                        .param("oldPassword", "oldPass")
                        .param("newPassword", "123"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("""
            PATCH /users/me/change-password | Returns 401 when not authenticated
            """)
    void changePassword_NotAuthenticated_ShouldReturn401() throws Exception {
        mockMvc.perform(patch("/users/me/change-password")
                        .param("oldPassword", "oldPass")
                        .param("newPassword", "newPass123"))
                .andExpect(status().isUnauthorized());
    }

    // ── verifyChange ──────────────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "john@test.com")
    @DisplayName("""
            POST /users/verify | Returns updated user after successful verification
            """)
    void verifyChange_ValidCode_ShouldReturnUpdatedUser() throws Exception {
        // Given
        when(userService.verifyChange("john@test.com", "123456"))
                .thenReturn(buildUserResponse());

        // When / Then
        mockMvc.perform(post("/users/verify")
                        .param("request", "123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john@test.com"));
    }

    @Test
    @WithMockUser(username = "john@test.com")
    @DisplayName("""
            POST /users/verify | Returns 404 when user not found
            """)
    void verifyChange_UserNotFound_ShouldReturn404() throws Exception {
        // Given
        when(userService.verifyChange(eq("john@test.com"), any()))
                .thenThrow(new EntityNotFoundException("User", "john@test.com"));

        // When / Then
        mockMvc.perform(post("/users/verify")
                        .param("request", "badcode"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("""
            POST /users/verify | Returns 401 when not authenticated
            """)
    void verifyChange_NotAuthenticated_ShouldReturn401() throws Exception {
        mockMvc.perform(post("/users/verify")
                        .param("request", "123456"))
                .andExpect(status().isUnauthorized());
    }
}
