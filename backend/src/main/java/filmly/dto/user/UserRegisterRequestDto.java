package filmly.dto.user;

public record UserRegisterRequestDto(
        String firstName,
        String lastName,
        String email,
        String password,
        String confirmPassword
) {}
