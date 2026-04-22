package filmly.dto.contentrating;

import filmly.model.Content;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ContentRatingUpdateRequestDto(
        @NotNull Long contentId,
        @NotNull Content.ContentType contentType,
        @Min(1) @Max(10) Float rating,
        @Size(max = 1000) String review
) {}
