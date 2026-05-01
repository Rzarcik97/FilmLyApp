package filmly.dto.contentlikes;

import filmly.model.Content;
import jakarta.validation.constraints.NotNull;

public record ContentLikeRequestDto(
        @NotNull Long contentId,
        @NotNull Content.ContentType contentType,
        @NotNull Boolean isLike
) {}
