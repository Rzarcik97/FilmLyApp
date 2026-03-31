package filmly.controller;

import filmly.dto.content.CastDto;
import filmly.dto.content.ContentDto;
import filmly.dto.content.MovieDetailDto;
import filmly.service.TmdbContentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MoviesController {

    private final TmdbContentService<ContentDto,MovieDetailDto> movieService;

    @GetMapping("/popular")
    public List<ContentDto> getPopularMovies() {
        return movieService.findPopular();
    }

    @GetMapping("/trending")
    public List<ContentDto> getTrendingMovies() {
        return movieService.findTrending();
    }

    @GetMapping("/recent")
    public List<ContentDto> getRecentMovies() {
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
    public List<ContentDto> getMovieSimilar(@PathVariable Long id) {
        return movieService.findSimilar(id);
    }

    @Deprecated
    @GetMapping("/recommendation")
    public ResponseEntity<List<ContentDto>> getRecommendations() {
        // TODO: implement
        return ResponseEntity.ok().build();
    }
}
