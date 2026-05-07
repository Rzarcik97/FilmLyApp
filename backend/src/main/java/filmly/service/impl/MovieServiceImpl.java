package filmly.service.impl;

import filmly.dto.content.CastDto;
import filmly.dto.content.ContentDto;
import filmly.dto.content.MovieDetailDto;
import filmly.dto.content.ScoreContent;
import filmly.dto.contentlikes.ContentLikeResponseDto;
import filmly.dto.tmdb.TmdbContentResponse;
import filmly.dto.tmdb.TmdbContentResult;
import filmly.dto.tmdb.TmdbCreditsResponse;
import filmly.dto.tmdb.TmdbMovieDetailResponse;
import filmly.enums.GenreType;
import filmly.exception.EntityNotFoundException;
import filmly.mapper.MovieMapper;
import filmly.model.Content;
import filmly.service.ContentLikeService;
import filmly.service.FavoriteGenreService;
import filmly.service.MovieService;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Log4j2
@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final RestClient restClient;
    private final MovieMapper movieMapper;
    private final ContentLikeService contentLikeService;
    private final FavoriteGenreService favoriteGenreService;
    private final RecommendationScorer scorer;

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

        List<TmdbContentResult> results = response.results();
        List<Long> contentIds = results.stream()
                .map(TmdbContentResult::id)
                .toList();

        Map<Long, ContentLikeResponseDto> likesMap = contentLikeService.getLikesByContentIds(
                contentIds, Content.ContentType.MOVIE);

        return response.results().stream()
                .limit(10)
                .map(r -> {
                    ContentLikeResponseDto likes = likesMap.get(r.id());
                    return movieMapper.toDto(r, likes.likes(),likes.dislikes());
                })
                .toList();
    }

    @Override
    public List<ContentDto> findRecommendations(String email) {

        Map<Long, Double> userRatings = (email != null)
                ? favoriteGenreService.getUserGenreRatings(email)
                : Map.of();

        Map<Long, TmdbContentResult> unique = new HashMap<>();

        for (int page = 1; page <= 2; page++) {
            fetchRaw("/movie/popular?page=" + page).forEach(r -> unique.put(r.id(), r));
            fetchRaw("/movie/now_playing?page=" + page).forEach(r -> unique.put(r.id(), r));
            fetchRaw("/trending/movie/day?page=" + page).forEach(r -> unique.put(r.id(), r));
        }

        List<Long> genreIds = userRatings.isEmpty()
                ? favoriteGenreService.getRandomGenreIds(GenreType.MOVIE)
                : userRatings.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(4)
                .map(Map.Entry::getKey)
                .toList();

        for (Long genreId : genreIds) {
            for (int page = 1; page <= 2; page++) {
                fetchRawDiscover(genreId, page).forEach(r -> unique.put(r.id(), r));
            }
        }

        log.info("Pool size: {}", unique.size());

        return unique.values().stream()
                .map(r -> {
                    double finalScore = scorer.calculateFinalScore(r, userRatings);
                    return new ScoreContent(r, finalScore);
                })
                .sorted(Comparator.comparingDouble(ScoreContent::score).reversed())
                .limit(20)
                .map(sc -> mapToDto(sc.result()))
                .toList();
    }

    @Override
    public MovieDetailDto findById(Long id, String email) {

        Boolean isLiked = null;

        if (email != null) {
            isLiked = contentLikeService.isLiked(email, id, Content.ContentType.MOVIE);
        }

        TmdbMovieDetailResponse response = restClient.get()
                .uri("/movie/{id}?append_to_response=videos", id)
                .retrieve()
                .body(TmdbMovieDetailResponse.class);
        if (response == null) {
            throw new EntityNotFoundException("Movie", id);
        }
        ContentLikeResponseDto likes = contentLikeService.getLikes(id, Content.ContentType.MOVIE);
        return movieMapper.toDetailDto(response, likes.likes(), likes.dislikes(), isLiked);
    }

    private List<ContentDto> fetch(String uri) {
        List<TmdbContentResult> results = fetchRaw(uri);
        List<Long> contentIds = results.stream()
                .map(TmdbContentResult::id)
                .toList();

        Map<Long, ContentLikeResponseDto> likesMap = contentLikeService.getLikesByContentIds(
                contentIds, Content.ContentType.MOVIE);

        return results.stream()
                .map(r -> {
                    ContentLikeResponseDto likes = likesMap.get(r.id());
                    return movieMapper.toDto(r, likes.likes(),likes.dislikes());
                })
                .toList();
    }

    private List<TmdbContentResult> fetchRaw(String uri) {
        TmdbContentResponse response = restClient.get()
                .uri(uri)
                .retrieve()
                .body(TmdbContentResponse.class);

        if (response == null || response.results() == null) {
            return List.of();
        }

        return response.results();
    }

    private List<TmdbContentResult> fetchRawDiscover(Long genreId, int page) {
        TmdbContentResponse response = restClient.get()
                .uri("/discover/movie?with_genres={genreId}&page={page}", genreId, page)
                .retrieve()
                .body(TmdbContentResponse.class);

        if (response == null || response.results() == null) {
            return List.of();
        }

        return response.results();
    }

    private ContentDto mapToDto(TmdbContentResult r) {
        ContentLikeResponseDto likes = contentLikeService
                .getLikes(r.id(), Content.ContentType.MOVIE);

        return movieMapper.toDto(r, likes.likes(), likes.dislikes());
    }
}
