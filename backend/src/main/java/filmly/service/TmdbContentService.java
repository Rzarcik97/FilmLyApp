package filmly.service;

import java.util.List;
import java.util.Optional;

public interface TmdbContentService<T> {
    List<T> findPopular();

    List<T> findTrending();

    List<T> findRecent();

    List<T> findRecommendations(Long userId);

    List<String> findAllGenres();

    Optional<T> findById(Long id);
}
