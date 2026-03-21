package filmly.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/series")
@RequiredArgsConstructor
public class SeriesController {

    @GetMapping("/popular")
    public ResponseEntity<?> getPopularSeries() {
        // TODO: implement
        return ResponseEntity.ok().build();
    }

    @GetMapping("/trending")
    public ResponseEntity<?> getTrendingSeries() {
        // TODO: implement
        return ResponseEntity.ok().build();
    }

    @GetMapping("/recent")
    public ResponseEntity<?> getRecentSeries() {
        // TODO: implement
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSeriesById(@PathVariable Long id) {
        // TODO: implement
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchSeries(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String productionDateFrom,
            @RequestParam(required = false) String productionDateTo,
            @RequestParam(required = false) Double ratingMin,
            @RequestParam(required = false) Double ratingMax,
            @RequestParam(required = false) String genre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        // TODO: implement
        return ResponseEntity.ok().build();
    }

    @GetMapping("/recommendation")
    public ResponseEntity<?> getRecommendations() {
        // TODO: resolve current user from SecurityContext
        // TODO: implement
        return ResponseEntity.ok().build();
    }

}
