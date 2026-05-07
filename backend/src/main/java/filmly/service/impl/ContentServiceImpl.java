package filmly.service.impl;

import filmly.dto.tmdb.TmdbGenreDto;
import filmly.dto.tmdb.TmdbMovieDetailResponse;
import filmly.dto.tmdb.TmdbSeriesDetailResponse;
import filmly.exception.EntityNotFoundException;
import filmly.model.Content;
import filmly.model.Genre;
import filmly.repository.ContentRepository;
import filmly.repository.GenreRepository;
import filmly.service.ContentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Log4j2
@Service
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {

    private final ContentRepository contentRepository;
    private final GenreRepository genreRepository;
    private final RestClient restClient;

    @Override
    public Content getOrCreate(Long externalId, Content.ContentType type) {
        return contentRepository.findByExternalIdAndType(externalId, type)
                .orElseGet(() -> {
                    Content content = new Content();
                    content.setExternalId(externalId);
                    content.setType(type);
                    return contentRepository.save(populateContentDetails(content));
                });
    }

    private Content populateContentDetails(Content content) {
        if (content.getType() == Content.ContentType.MOVIE) {
            TmdbMovieDetailResponse movie = findMovieDetails(content.getExternalId());
            content.setTitle(movie.title());
            content.setPosterPath(movie.posterPath());
            content.setReleaseDate(movie.releaseDate());
            content.setVoteAverage(movie.voteAverage());
            content.setVoteCount(movie.voteCount());
            content.setGenres(resolveGenres(
                    movie.genres().stream().map(TmdbGenreDto::name).toList()));
        } else {
            TmdbSeriesDetailResponse series = findSeriesDetails(content.getExternalId());
            content.setTitle(series.name());
            content.setPosterPath(series.posterPath());
            content.setReleaseDate(series.firstAirDate());
            content.setVoteAverage(series.voteAverage());
            content.setVoteCount(series.voteCount());
            content.setGenres(resolveGenres(
                    series.genres().stream().map(TmdbGenreDto::name).toList()));
        }
        return content;
    }

    public TmdbMovieDetailResponse findMovieDetails(Long id) {
        TmdbMovieDetailResponse response = restClient.get()
                .uri("/movie/{id}?append_to_response=videos", id)
                .retrieve()
                .body(TmdbMovieDetailResponse.class);
        if (response == null) {
            throw new EntityNotFoundException("Movie", id);
        }
        return response;
    }

    public TmdbSeriesDetailResponse findSeriesDetails(Long id) {
        TmdbSeriesDetailResponse response = restClient.get()
                .uri("/tv/{id}?append_to_response=videos", id)
                .retrieve()
                .body(TmdbSeriesDetailResponse.class);
        if (response == null) {
            throw new EntityNotFoundException("Movie", id);
        }
        return response;
    }

    private List<Genre> resolveGenres(List<String> names) {
        return genreRepository.findAllByNameIn(names);
    }
}
