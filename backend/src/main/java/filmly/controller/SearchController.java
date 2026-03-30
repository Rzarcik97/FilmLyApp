package filmly.controller;

import filmly.dto.content.ContentDto;
import filmly.dto.search.DiscoverRequest;
import filmly.dto.search.PagedResponse;
import filmly.enums.SortBy;
import filmly.model.Content;
import filmly.service.SearchService;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public PagedResponse<ContentDto> search(
            @RequestParam String title,
            @RequestParam(required = false) Content.ContentType type,
            @RequestParam(defaultValue = "1") Integer page) {
        return searchService.search(title,type,page);
    }

    @GetMapping("/discover")
    public PagedResponse<ContentDto> discover(
            @RequestParam(required = false) @Schema(example = "7.0") Double ratingMin,
            @RequestParam(required = false) @Schema(example = "10.0") Double ratingMax,
            @RequestParam(required = false) @Schema(example = "2020-01-01") String dateFrom,
            @RequestParam(required = false) @Schema(example = "2026-12-31") String dateTo,
            @RequestParam(required = false) List<Long> genreIds,
            @RequestParam @Schema(example = "MOVIE") Content.ContentType type,
            @RequestParam(required = false) SortBy sortBy,
            @RequestParam(defaultValue = "1") @Schema(example = "1") Integer page) {
        DiscoverRequest discoverRequest = new DiscoverRequest();
        discoverRequest.setRatingMin(ratingMin);
        discoverRequest.setRatingMax(ratingMax);
        discoverRequest.setDateFrom(dateFrom);
        discoverRequest.setDateTo(dateTo);
        discoverRequest.setGenreIds(genreIds);
        discoverRequest.setType(type);
        discoverRequest.setSortBy(sortBy);
        discoverRequest.setPage(page);
        return searchService.discover(discoverRequest);
    }
}
