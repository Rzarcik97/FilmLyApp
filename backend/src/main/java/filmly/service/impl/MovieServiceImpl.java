package filmly.service.impl;

import filmly.dto.content.CastDto;
import filmly.dto.content.ContentDto;
import filmly.dto.content.MovieDetailDto;
import filmly.dto.tmdb.TmdbContentResponse;
import filmly.dto.tmdb.TmdbCreditsResponse;
import filmly.dto.tmdb.TmdbMovieDetailResponse;
import filmly.exception.EntityNotFoundException;
import filmly.mapper.MovieMapper;
import filmly.service.MovieService;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final RestClient restClient;

    private final MovieMapper movieMapper;

    @Override
    public List<ContentDto> findPopular() {
        return fetch("/movie/popular");
    }

    @Override
    public List<ContentDto> findTrending() {
        return fetch("/trending/movie/day");
    }

    @Override
    public List<ContentDto> findRecent() {
        return fetch("/movie/now_playing");
    }

    @Override
    public List<ContentDto> findUpcoming() {
        String today = LocalDate.now().toString();
        String future = LocalDate.now().plusMonths(3).toString();
        return fetch("/discover/movie?primary_release_date.gte=" + today
                + "&primary_release_date.lte=" + future
                + "&sort_by=popularity.desc");
    }

    @Override
    public List<CastDto> findCast(Long id) {
        TmdbCreditsResponse response = restClient.get()
                .uri("/movie/{id}/credits", id)
                .retrieve()
                .body(TmdbCreditsResponse.class);

        if (response == null || response.cast() == null) {
            throw new EntityNotFoundException("Movie", id);
        }

        return response.cast().stream()
                .sorted(Comparator.comparingInt(CastDto::order))
                .limit(10)
                .toList();
    }

    @Override
    public List<ContentDto> findSimilar(Long id) {
        TmdbContentResponse response = restClient.get()
                .uri("/movie/{id}/similar", id)
                .retrieve()
                .body(TmdbContentResponse.class);

        if (response == null || response.results() == null) {
            throw new EntityNotFoundException("Movie", id);
        }

        return response.results().stream()
                .limit(10)
                .map(movieMapper::toDto)
                .toList();
    }

    @Override
    public List<ContentDto> findRecommendations(Long userId) {

        return List.of();
    }

    @Override
    public MovieDetailDto findById(Long id) {
        TmdbMovieDetailResponse response = restClient.get()
                .uri("/movie/{id}?append_to_response=videos", id)
                .retrieve()
                .body(TmdbMovieDetailResponse.class);
        if (response == null) {
            throw new EntityNotFoundException("Movie", id);
        }
        return movieMapper.toDetailDto(response);
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
                .map(movieMapper::toDto)
                .toList();
    }
}
