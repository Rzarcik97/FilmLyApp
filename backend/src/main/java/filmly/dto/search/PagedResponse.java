package filmly.dto.search;

import java.util.List;

public record PagedResponse<T>(
        List<T> results,
        int page,
        int totalPages,
        int totalResults
) {}
