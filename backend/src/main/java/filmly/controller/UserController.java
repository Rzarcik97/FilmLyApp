package filmly.controller;

import filmly.dto.user.UserPatchRequestDto;
import filmly.dto.user.UserResponseDto;
import filmly.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @Operation(summary = "Get My Profile Info",
            description = "Retrieve profile information of the currently authenticated user")
    public UserResponseDto getMyProfile(Authentication authentication) {
        String email = authentication.getName();
        return userService.getMyProfile(email);
    }

    @PatchMapping("/me")
    @Operation(summary = "Update My Profile Info",
            description = "Update profile details of the currently authenticated user")
    public UserResponseDto updateMyProfile(@RequestBody @Valid UserPatchRequestDto request,
                                           Authentication authentication) {
        String email = authentication.getName();
        log.info("Editing User {} profile", email);
        return userService.updateMyProfile(email, request);
    }

    @PatchMapping("/me/change-email")
    @Operation(summary = "Change User Email",
            description = "Change the email of a user, returns the verification code "
                    + "that should be used in the /verify endpoint (requires authentication)")
    public String changeEmail(@RequestParam @Valid @Email String newEmail,
                              @RequestParam String currentPassword,
                              Authentication authentication) {
        String email = authentication.getName();
        log.info("User's {} changing email request", email);
        return userService.changeEmail(email, newEmail, currentPassword);
    }

    @PatchMapping("/me/change-password")
    @Operation(summary = "Change User Password",
            description = "Change the password of a user, returns the verification code "
                    + "that should be used in the /verify endpoint (requires authentication)")
    public String changePassword(@RequestParam String oldPassword,
                                 @RequestParam @Valid @NotBlank @Size(min = 6) String newPassword,
                                 Authentication authentication) {
        String email = authentication.getName();
        log.info("User's {} changing password request", email);
        return userService.changePassword(email, oldPassword, newPassword);
    }

    @PostMapping("/verify")
    @Operation(summary = "Verify code for email/password change",
            description = "Verify a previously requested email or password change"
                    + " using a verification code, after successful verification"
                    + " user should login again")
    public UserResponseDto verifyChange(@RequestParam String request,
                                        Authentication authentication) {
        String email = authentication.getName();
        log.info("User's {} verification code request", email);
        return userService.verifyChange(email, request);
    }
}
