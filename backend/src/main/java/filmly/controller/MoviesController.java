package filmly.controller;

import filmly.dto.content.MovieDto;
import filmly.service.ContentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MoviesController {

    private final ContentService contentService;

    @GetMapping("/popular")
    public ResponseEntity<List<MovieDto>> getPopularMovies() {
        // TODO: implement
        return ResponseEntity.ok().build();
    }

    @GetMapping("/trending")
    public ResponseEntity<List<MovieDto>> getTrendingMovies() {
        // TODO: implement
        return ResponseEntity.ok().build();
    }

    @GetMapping("/recent")
    public ResponseEntity<List<MovieDto>> getRecentMovies() {
        // TODO: implement
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> getMovieById(@PathVariable Long id) {
        // TODO: implement
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<MovieDto>> searchMovies(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String productionDate,
            @RequestParam(required = false) Double rating
    ) {
        // TODO: implement
        return ResponseEntity.ok().build();
    }

    @GetMapping("/genres")
    public ResponseEntity<List<String>> getGenres() {
        // TODO: implement
        return ResponseEntity.ok().build();
    }

    @GetMapping("/recommendation")
    public ResponseEntity<List<MovieDto>> getRecommendations() {
        // TODO: implement
        return ResponseEntity.ok().build();
    }
}
