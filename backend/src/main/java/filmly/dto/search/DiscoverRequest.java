package filmly.dto.search;

import filmly.enums.SortBy;
import filmly.model.Content;
import java.util.List;
import lombok.Data;

@Data
public class DiscoverRequest {
    private Double ratingMin;
    private Double ratingMax;
    private String dateFrom;
    private String dateTo;
    private List<Long> genreIds;
    private Content.ContentType type;
    private SortBy sortBy;
    private Integer page = 1;
}
