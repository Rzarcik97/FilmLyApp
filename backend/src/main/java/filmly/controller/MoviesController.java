package filmly.controller;

import filmly.dto.content.CastDto;
import filmly.dto.content.ContentDto;
import filmly.dto.content.MovieDetailDto;
import filmly.exception.ErrorResponse;
import filmly.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    private final MovieService movieService;

    @GetMapping("/popular")
    @Operation(summary = "Get popular movies, source TMDB")
    public List<ContentDto> getPopularMovies() {
        return movieService.findPopular();
    }

    @GetMapping("/trending")
    @Operation(summary = "Get trending movies, source TMDB")
    public List<ContentDto> getTrendingMovies() {
        return movieService.findTrending();
    }

    @GetMapping("/recent")
    @Operation(summary = "Get recent movies, source TMDB")
    public List<ContentDto> getRecentMovies() {
        return movieService.findRecent();
    }

    @Operation(summary = "Get upcoming movies, source TMDB",
            description = "Returns upcoming movies sorted by TMDB popularity.Popularity "
                    + "is based on page views, watchlist adds and user engagement on TMDB.")
    @GetMapping("/upcoming")
    public List<ContentDto> getUpcomingMovies() {
        return movieService.findUpcoming();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get movie by ID, source TMDB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = MovieDetailDto.class))),
            @ApiResponse(responseCode = "404", description = "Movie not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "429", description = "Too many requests to TMDB",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public MovieDetailDto getMovieById(@PathVariable Long id) {
        return movieService.findById(id);
    }

    @GetMapping("/{id}/cast")
    @Operation(summary = "Get movie cast, source TMDB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(array = @ArraySchema(
                            schema = @Schema(implementation = CastDto.class)))),
            @ApiResponse(responseCode = "404", description = "Movie not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "429", description = "Too many requests to TMDB",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public List<CastDto> getMovieCast(@PathVariable Long id) {
        return movieService.findCast(id);
    }

    @GetMapping("/{id}/similar")
    @Operation(summary = "Get similar movies, source TMDB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(array = @ArraySchema(
                            schema = @Schema(implementation = ContentDto.class)))),
            @ApiResponse(responseCode = "404", description = "Movie not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "429", description = "Too many requests to TMDB",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public List<ContentDto> getMovieSimilar(@PathVariable Long id) {
        return movieService.findSimilar(id);
    }

    @Deprecated
    @GetMapping("/recommendation")
    @Operation(summary = "Get movie recommendations, source FilmLy")
    public ResponseEntity<List<ContentDto>> getRecommendations() {
        // TODO: implement
        return ResponseEntity.ok().build();
    }
}
