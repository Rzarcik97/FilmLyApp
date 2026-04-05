package filmly.controller;

import filmly.dto.content.ContentDto;
import filmly.dto.content.SeriesDetailDto;
import filmly.service.TmdbContentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/series")
@CrossOrigin(origins = "http://localhost:5173") // Added for working with frontend as well
@RequiredArgsConstructor
public class SeriesController {

    private final TmdbContentService<ContentDto, SeriesDetailDto> seriesService;

    @GetMapping("/popular")
    public List<ContentDto> getPopularSeries() {
        return seriesService.findPopular();
    }

    @GetMapping("/trending")
    public List<ContentDto> getTrendingSeries() {
        return seriesService.findTrending();
    }

    @GetMapping("/recent")
    public List<ContentDto> getRecentSeries() {
        return seriesService.findRecent();
    }

    @GetMapping("/{id}")
    public SeriesDetailDto getSeriesById(@PathVariable Long id) {
        return seriesService.findById(id);
    }

    @Deprecated
    @GetMapping("/recommendation")
    public ResponseEntity<?> getRecommendations() {
        // TODO: resolve current user from SecurityContext
        // TODO: implement
        return ResponseEntity.ok().build();
    }

}
