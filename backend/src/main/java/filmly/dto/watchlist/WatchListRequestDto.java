package filmly.dto.watchlist;

import filmly.model.Content;

public record WatchListRequestDto(
        Long contentId,
        Content.ContentType contentType
) {}
