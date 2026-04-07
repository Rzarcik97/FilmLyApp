package filmly.dto.user;

public record UserRegisterRequestDto(
        String username,
        String firstName,
        String lastName,
        String email,
        String password,
        String confirmPassword
) {}
