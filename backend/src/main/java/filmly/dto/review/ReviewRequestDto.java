package filmly.dto.review;

public record ReviewRequestDto(
        Long contentId,
        String reviewText
) {}
