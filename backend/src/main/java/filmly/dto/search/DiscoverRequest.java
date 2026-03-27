package filmly.dto.search;

import filmly.model.Content;
import java.util.List;

public record DiscoverRequest(
        Double ratingMin,
        Double ratingMax,
        String dateFrom,
        String dateTo,
        List<Long> genreIds,
        Content.ContentType type,
        String sortBy,
        Integer page
) {}
