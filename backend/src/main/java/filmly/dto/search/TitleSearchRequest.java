package filmly.dto.search;

import filmly.model.Content;

public record TitleSearchRequest(
        String title,
        Content.ContentType type
) {}
