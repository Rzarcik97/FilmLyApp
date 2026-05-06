package filmly.repository;

import filmly.dto.contentlikes.ContentLikeCountProjection;
import filmly.model.Content;
import filmly.model.ContentLike;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContentLikeRepository extends JpaRepository<ContentLike, Long> {

    @Query("SELECT cl FROM ContentLike cl "
            + "JOIN FETCH cl.content c "
            + "LEFT JOIN FETCH c.genres "
            + "WHERE cl.user.id = :userId "
            + "AND c.type = :type "
            + "AND cl.liked = :liked")
    List<ContentLike> findByUser_IdAndContent_TypeAndLiked(
            @Param("userId") Long userId,
            @Param("type") Content.ContentType type,
            @Param("liked") Boolean liked);

    @Query("SELECT cl.content.externalId as contentId, "
            + "COALESCE(SUM(CASE WHEN cl.liked = true THEN 1 ELSE 0 END), 0) as likes, "
            + "COALESCE(SUM(CASE WHEN cl.liked = false THEN 1 ELSE 0 END), 0) as dislikes "
            + "FROM ContentLike cl WHERE cl.content.externalId IN :contentIds "
            + "AND cl.content.type = :contentType GROUP BY cl.content.externalId")
    List<ContentLikeCountProjection> countLikesAndDislikesByContentIds(
            @Param("contentIds") List<Long> contentIds,
            @Param("contentType") Content.ContentType contentType);

    Long countByContent_ExternalIdAndContent_TypeAndLiked(
            Long contentExternalId, Content.ContentType contentType, Boolean liked);

    Optional<ContentLike> findByUser_IdAndContent_ExternalIdAndContent_Type(
            Long userId, Long contentExternalId, Content.ContentType contentType);
}
