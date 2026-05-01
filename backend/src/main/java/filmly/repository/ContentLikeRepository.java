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

    Optional<ContentLike> findByUser_IdAndContentIdAndContentType(
            Long userId, Long contentId, Content.ContentType contentType);

    Long countByContentIdAndContentTypeAndLiked(
            Long contentId, Content.ContentType contentType, Boolean liked);

    @Query("SELECT cl.contentId as contentId, "
            + "COALESCE(SUM(CASE WHEN cl.liked = true THEN 1 ELSE 0 END), 0) as likes, "
            + "COALESCE(SUM(CASE WHEN cl.liked = false THEN 1 ELSE 0 END), 0) as dislikes "
            + "FROM ContentLike cl WHERE cl.contentId IN :contentIds "
            + "AND cl.contentType = :contentType GROUP BY cl.contentId")
    List<ContentLikeCountProjection> countLikesAndDislikesByContentIds(
            @Param("contentIds") List<Long> contentIds,
            @Param("contentType") Content.ContentType contentType);
}
