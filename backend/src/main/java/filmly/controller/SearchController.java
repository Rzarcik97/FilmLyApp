package filmly.controller;

import filmly.dto.content.ContentDto;
import filmly.model.Content;
import filmly.service.SearchService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public List<ContentDto> search(
            @RequestParam String title,
            @RequestParam(required = false) Content.ContentType type) {
        return searchService.search(title,type);
    }

    @GetMapping("/discover")
    public ResponseEntity<?> discover(
            @RequestParam(required = false) Double ratingMin,
            @RequestParam(required = false) Double ratingMax,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo,
            @RequestParam(required = false) List<Long> genreIds,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "1") Integer page
    ) {
        // TODO: implement
        return ResponseEntity.ok().build();
    }
}
