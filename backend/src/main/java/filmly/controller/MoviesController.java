package filmly.controller;

import filmly.dto.content.CastDto;
import filmly.dto.content.MovieDetailDto;
import filmly.dto.content.MovieDto;
import filmly.service.TmdbContentService;
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

    private final TmdbContentService<MovieDto,MovieDetailDto> movieService;

    @GetMapping("/popular")
    public List<MovieDto> getPopularMovies() {
        return movieService.findPopular();
    }

    @GetMapping("/trending")
    public List<MovieDto> getTrendingMovies() {
        return movieService.findTrending();
    }

    @GetMapping("/recent")
    public List<MovieDto> getRecentMovies() {
        return movieService.findRecent();
    }

    @GetMapping("/{id}")
    public MovieDetailDto getMovieById(@PathVariable Long id) {
        return movieService.findById(id);
    }

    @GetMapping("/{id}/cast")
    public List<CastDto> getMovieCast(@PathVariable Long id) {
        return movieService.findCast(id);
    }

    @GetMapping("/{id}/similar")
    public List<MovieDto> getMovieSimilar(@PathVariable Long id) {
        return movieService.findSimilar(id);
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

    @GetMapping("/recommendation")
    public ResponseEntity<List<MovieDto>> getRecommendations() {
        // TODO: implement
        return ResponseEntity.ok().build();
    }
}
