package filmly.controller;

import filmly.dto.content.CastDto;
import filmly.dto.content.ContentDto;
import filmly.dto.content.SeriesDetailDto;
import filmly.exception.ErrorResponse;
import filmly.service.TmdbContentService;
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
@RequestMapping("/series")
@RequiredArgsConstructor
public class SeriesController {

    private final TmdbContentService<ContentDto, SeriesDetailDto> seriesService;

    @GetMapping("/popular")
    @Operation(summary = "Get popular series, source TMDB")
    public List<ContentDto> getPopularSeries() {
        return seriesService.findPopular();
    }

    @GetMapping("/trending")
    @Operation(summary = "Get trending series, source TMDB")
    public List<ContentDto> getTrendingSeries() {
        return seriesService.findTrending();
    }

    @GetMapping("/recent")
    @Operation(summary = "Get recent series, source TMDB")
    public List<ContentDto> getRecentSeries() {
        return seriesService.findRecent();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get series by ID, source TMDB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = SeriesDetailDto.class))),
            @ApiResponse(responseCode = "404", description = "Series not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "429", description = "Too many requests to TMDB",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public SeriesDetailDto getSeriesById(@PathVariable Long id) {
        return seriesService.findById(id);
    }

    @GetMapping("/{id}/similar")
    @Operation(summary = "Get similar series, source TMDB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(array = @ArraySchema(
                            schema = @Schema(implementation = ContentDto.class)))),
            @ApiResponse(responseCode = "404", description = "Series not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "429", description = "Too many requests to TMDB",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public List<ContentDto> getSeriesSimilar(@PathVariable Long id) {
        return seriesService.findSimilar(id);
    }

    @GetMapping("/{id}/cast")
    @Operation(summary = "Get series by ID, source TMDB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(array = @ArraySchema(
                            schema = @Schema(implementation = CastDto.class)))),
            @ApiResponse(responseCode = "404", description = "Series not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "429", description = "Too many requests to TMDB",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public List<CastDto> getSeriesCast(@PathVariable Long id) {
        return seriesService.findCast(id);
    }

    @Deprecated
    @GetMapping("/recommendation")
    @Operation(summary = "Get series recommendations, source FilmLy")
    public ResponseEntity<?> getRecommendations() {
        // TODO: resolve current user from SecurityContext
        // TODO: implement
        return ResponseEntity.ok().build();
    }

}
