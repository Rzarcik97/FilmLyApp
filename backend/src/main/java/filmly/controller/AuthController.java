package filmly.controller;

import filmly.dto.user.UserLoginRequestDto;
import filmly.dto.user.UserLoginResponseDto;
import filmly.dto.user.UserRegisterRequestDto;
import filmly.dto.user.UserResponseDto;
import filmly.exception.ErrorResponse;
import filmly.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/registration")
    @Operation(summary = "Register User", description = "Register new user in dataBase")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    UserResponseDto register(@RequestBody @Valid UserRegisterRequestDto request) {
        return authenticationService.register(request);
    }

    @PostMapping("/login")
    @Operation(summary = "Login User", description = "Authorize user with dataBase")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully",
                    content = @Content(schema = @Schema(
                            implementation = UserLoginResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto request) {
        return authenticationService.login(request);
    }
}
