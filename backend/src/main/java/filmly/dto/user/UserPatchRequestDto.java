package filmly.dto.user;

public record UserPatchRequestDto(
        String username,
        String firstName,
        String lastName,
        String avatarUrl
) {}
