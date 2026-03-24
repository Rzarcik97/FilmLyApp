package filmly.service.impl;

import filmly.dto.content.MovieDto;
import filmly.dto.tmdb.TmdbMovieResponse;
import filmly.mapper.MovieMapper;
import filmly.service.TmdbContentService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements TmdbContentService<MovieDto> {

    private final RestClient restClient;

    private final MovieMapper movieMapper;

    @Override
    public List<MovieDto> findPopular() {
        return fetch("/movie/popular");
    }

    @Override
    public List<MovieDto> findTrending() {
        return fetch("/trending/movie/day");
    }

    public List<MovieDto> findRecent() {
        return fetch("/movie/now_playing");
    }

    @Override
    public List<MovieDto> findRecommendations(Long userId) {
        return List.of();
    }

    @Override
    public List<String> findAllGenres() {
        return List.of();
    }

    @Override
    public Optional<MovieDto> findById(Long id) {
        return Optional.empty();
    }

    private List<MovieDto> fetch(String uri) {
        TmdbMovieResponse response = restClient.get()
                .uri(uri)
                .retrieve()
                .body(TmdbMovieResponse.class);

        if (response == null || response.results() == null) {
            return List.of();
        }

        return response.results().stream()
                .map(movieMapper::toDto)
                .toList();
    }
}
