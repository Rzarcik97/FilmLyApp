package filmly.repository;

import filmly.model.Content;
import filmly.model.ContentRating;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContentRatingRepository extends JpaRepository<ContentRating, Long> {

    Optional<ContentRating> findByUser_IdAndContentIdAndContentType(
            Long userId, Long contentId, Content.ContentType contentType);

    Optional<ContentRating> findById(Long id);

    @Query("SELECT cr FROM ContentRating cr "
            + "WHERE cr.contentId = :contentId "
            + "AND cr.contentType = :contentType "
            + "ORDER BY cr.createdAt DESC LIMIT 10")
    List<ContentRating> findLatestByContentIdAndContentType(
            @Param("contentId") Long contentId,
            @Param("contentType") Content.ContentType contentType);
}
