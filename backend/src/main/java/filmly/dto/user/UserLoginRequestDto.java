package filmly.dto.user;

public record UserLoginRequestDto(
        String email,
        String password
) {}
