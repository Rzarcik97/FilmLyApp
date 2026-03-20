package filmly.dto.user;

import java.time.LocalDateTime;

public record UserResponseDto(
        Long userId,
        String firstName,
        String lastName,
        String email,
        String avatarUrl,
        LocalDateTime createdAt
) {}
