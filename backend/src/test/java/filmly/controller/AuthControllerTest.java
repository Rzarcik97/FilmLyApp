package filmly.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import filmly.dto.user.UserLoginRequestDto;
import filmly.dto.user.UserLoginResponseDto;
import filmly.dto.user.UserRegisterRequestDto;
import filmly.dto.user.UserResponseDto;
import filmly.exception.AuthenticationException;
import filmly.exception.RegistrationException;
import filmly.service.AuthenticationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

public class AuthControllerTest extends BaseControllerTest {

    @MockitoBean
    private AuthenticationService authenticationService;

    @Autowired
    private ObjectMapper objectMapper;

    // ── register ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("""
            POST /auth/registration | Returns UserResponseDto when registration successful
            """)
    void register_ValidRequest_ShouldReturnUserDto() throws Exception {
        // Given
        UserRegisterRequestDto request = new UserRegisterRequestDto(
                "john_doe", "John", "Doe",
                "john@test.com", "password123", "password123"
        );

        UserResponseDto response = new UserResponseDto(
                1L, "john_doe", "John", "Doe",
                "john@test.com", null, null
        );

        when(authenticationService.register(any())).thenReturn(response);

        // When / Then
        mockMvc.perform(post("/auth/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.username").value("john_doe"))
                .andExpect(jsonPath("$.email").value("john@test.com"));
    }

    @Test
    @DisplayName("""
            POST /auth/registration | Returns 400 when request is invalid
            """)
    void register_InvalidRequest_ShouldReturn400() throws Exception {
        // Given — missing required fields
        String invalidRequest = """
                {
                    "username": "",
                    "email": "not-an-email",
                    "password": "123"
                }
                """;

        // When / Then
        mockMvc.perform(post("/auth/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("""
            POST /auth/registration | Returns 400 when email already exists
            """)
    void register_EmailAlreadyExists_ShouldReturn400() throws Exception {
        // Given
        UserRegisterRequestDto request = new UserRegisterRequestDto(
                "john_doe", "John", "Doe",
                "john@test.com", "password123", "password123"
        );

        when(authenticationService.register(any()))
                .thenThrow(new RegistrationException("Email already exists"));

        // When / Then
        mockMvc.perform(post("/auth/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // ── login ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("""
            POST /auth/login | Returns token when credentials are valid
            """)
    void login_ValidCredentials_ShouldReturnToken() throws Exception {
        // Given
        UserLoginRequestDto request = new UserLoginRequestDto(
                "john@test.com", "password123"
        );

        UserLoginResponseDto response = new UserLoginResponseDto("jwt.token.here");

        when(authenticationService.login(any())).thenReturn(response);

        // When / Then
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt.token.here"));
    }

    @Test
    @DisplayName("""
            POST /auth/login | Returns 401 when credentials are invalid
            """)
    void login_InvalidCredentials_ShouldReturn401() throws Exception {
        // Given
        UserLoginRequestDto request = new UserLoginRequestDto(
                "john@test.com", "wrongpassword"
        );

        when(authenticationService.login(any()))
                .thenThrow(new AuthenticationException("Invalid credentials"));

        // When / Then
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
}
