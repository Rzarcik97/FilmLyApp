package filmly.repository;

import filmly.model.Content;
import filmly.model.WatchList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WatchListRepository extends JpaRepository<WatchList, Long> {

    @Query("SELECT w FROM WatchList w "
            + "JOIN FETCH w.content c "
            + "LEFT JOIN FETCH c.genres "
            + "WHERE w.user.id = :userId "
            + "AND c.type = :type")
    List<WatchList> findByUser_IdAndContent_Type(
            @Param("userId") Long userId,
            @Param("type") Content.ContentType type);

    void deleteByUser_IdAndContentIdAndContentType(
            Long userId, Long contentId, Content.ContentType contentType);

    @Query("SELECT w FROM WatchList w "
            + "JOIN FETCH w.content c "
            + "LEFT JOIN FETCH c.genres "
            + "WHERE w.user.id = :userId "
            + "AND w.watchedAt IS NULL "
            + "AND c.type = :type")
    List<WatchList> findByUser_IdAndWatchedAtIsNullAndContent_Type(
            @Param("userId") Long userId,
            @Param("type") Content.ContentType type);

    Optional<WatchList> findByUser_IdAndContent_ExternalIdAndContent_Type(
            Long userId,
            Long contentExternalId,
            Content.ContentType contentType);
}
