package filmly.repository;

import filmly.model.Content;
import filmly.model.WatchList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WatchListRepository extends JpaRepository<WatchList, Long> {

    List<WatchList> findByUser_Id(Long userId);

    Optional<WatchList> findByUser_IdAndContentIdAndContentType(
            Long userId, Long contentId, Content.ContentType contentType);

    void deleteByUser_IdAndContentIdAndContentType(
            Long userId, Long contentId, Content.ContentType contentType);

    List<WatchList> findByUser_IdAndWatchedAtIsNull(Long userId);
}
