package filmly.dto.user;

import jakarta.validation.constraints.NotBlank;

public record UserPatchRequestDto(
        @NotBlank String username,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String avatarUrl
) {}
