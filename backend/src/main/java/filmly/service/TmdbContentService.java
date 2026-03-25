package filmly.service;

import java.util.List;

public interface TmdbContentService<T, D> {
    List<T> findPopular();

    List<T> findTrending();

    List<T> findRecent();

    List<T> findRecommendations(Long userId);

    D findById(Long id);
}
