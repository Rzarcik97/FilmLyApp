package filmly.service;

import filmly.model.Content;
import java.util.List;
import java.util.Optional;

public interface ContentService {
    Content save(Content movieDto);

    Optional<Content> findById(Long id);

    List<Content> findAll();

    List<Content> findPopular();

    List<Content> findTrending();

    List<Content> findRecent();

    List<Content> findRecommendations(Long userId);

    List<String> findAllGenres();

    Content update(Long id, Content movieDto);

    void deleteById(Long id);
}
