package filmly.service;

import filmly.dto.content.CastDto;
import java.util.List;

public interface TmdbContentService<T, D> {
    List<T> findPopular();

    List<T> findTrending();

    List<T> findRecent();

    List<CastDto> findCast(Long id);

    List<T> findSimilar(Long id);

    List<T> findRecommendations(Long userId);

    D findById(Long id);
}
