package filmly.dto.session;

import java.time.LocalDateTime;

public record SessionDto(
        Long sessionId,
        Long userId,
        LocalDateTime sessionStart,
        LocalDateTime sessionEnd,
        String country,
        String deviceType,
        String browser,
        String language
) {}
