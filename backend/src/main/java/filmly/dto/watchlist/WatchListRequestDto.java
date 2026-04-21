package filmly.dto.watchlist;

import filmly.model.Content;
import jakarta.validation.constraints.NotNull;

public record WatchListRequestDto(
        @NotNull Long contentId,
        @NotNull Content.ContentType contentType
) {}
