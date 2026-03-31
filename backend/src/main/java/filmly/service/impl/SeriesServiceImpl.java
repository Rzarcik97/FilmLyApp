package filmly.service.impl;

import filmly.dto.content.CastDto;
import filmly.dto.content.ContentDto;
import filmly.dto.content.SeriesDetailDto;
import filmly.dto.tmdb.TmdbContentResponse;
import filmly.dto.tmdb.TmdbCreditsResponse;
import filmly.dto.tmdb.TmdbSeriesDetailResponse;
import filmly.exception.EntityNotFoundException;
import filmly.mapper.SeriesMapper;
import filmly.service.TmdbContentService;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class SeriesServiceImpl implements TmdbContentService<ContentDto, SeriesDetailDto> {

    private final RestClient restClient;

    private final SeriesMapper seriesMapper;

    @Override
    public List<ContentDto> findPopular() {
        return fetch("/tv/popular");
    }

    @Override
    public List<ContentDto> findTrending() {
        return fetch("/trending/tv/day");
    }

    @Override
    public List<ContentDto> findRecent() {
        return fetch("/tv/on_the_air");
    }

    @Override
    public List<CastDto> findCast(Long id) {
        TmdbCreditsResponse response = restClient.get()
                .uri("/tv/{id}/credits", id)
                .retrieve()
                .body(TmdbCreditsResponse.class);

        if (response == null || response.cast() == null) {
            throw new EntityNotFoundException("Series", id);
        }

        return response.cast().stream()
                .sorted(Comparator.comparingInt(CastDto::order))
                .limit(10)
                .toList();
    }

    @Override
    public List<ContentDto> findSimilar(Long id) {
        TmdbContentResponse response = restClient.get()
                .uri("/tv/{id}/recommendations", id)
                .retrieve()
                .body(TmdbContentResponse.class);

        if (response == null || response.results() == null) {
            throw new EntityNotFoundException("Series", id);
        }

        return response.results().stream()
                .limit(10)
                .map(seriesMapper::toDto)
                .toList();
    }

    @Override
    public List<ContentDto> findRecommendations(Long userId) {

        return List.of();
    }

    @Override
    public SeriesDetailDto findById(Long id) {
        TmdbSeriesDetailResponse response = restClient.get()
                .uri("/tv/{id}?append_to_response=videos", id)
                .retrieve()
                .body(TmdbSeriesDetailResponse.class);
        if (response == null) {
            throw new EntityNotFoundException("Series", id);
        }
        return seriesMapper.toDetailDto(response);
    }

    private List<ContentDto> fetch(String uri) {
        TmdbContentResponse response = restClient.get()
                .uri(uri)
                .retrieve()
                .body(TmdbContentResponse.class);

        if (response == null || response.results() == null) {
            return List.of();
        }

        return response.results().stream()
                .map(seriesMapper::toDto)
                .toList();
    }
}
