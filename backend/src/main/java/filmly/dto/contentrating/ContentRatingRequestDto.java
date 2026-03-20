package filmly.dto.contentrating;

public record ContentRatingRequestDto(
        Long contentId,
        Double rating
) {}
