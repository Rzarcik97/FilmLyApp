package filmly.service.impl;

import filmly.dto.content.MovieDetailDto;
import filmly.dto.content.MovieDto;
import filmly.dto.tmdb.TmdbMovieDetailResponse;
import filmly.dto.tmdb.TmdbMovieResponse;
import filmly.exception.EntityNotFoundException;
import filmly.mapper.MovieMapper;
import filmly.service.TmdbContentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements TmdbContentService<MovieDto, MovieDetailDto> {

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

    @Override
    public List<MovieDto> findRecent() {
        return fetch("/movie/now_playing");
    }

    @Override
    public List<MovieDto> findRecommendations(Long userId) {
        return List.of();
    }

    @Override
    public MovieDetailDto findById(Long id) {
        TmdbMovieDetailResponse response = restClient.get()
                .uri("/movie/{id}?append_to_response=credits", id)
                .retrieve()
                .body(TmdbMovieDetailResponse.class);
        if (response == null) {
            throw new EntityNotFoundException("Movie", id);
        }
        return movieMapper.toDetailDto(response);
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
