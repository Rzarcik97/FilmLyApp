package filmly.repository;

import filmly.model.Content;
import filmly.model.ContentRating;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRatingRepository extends JpaRepository<ContentRating, Long> {

    Optional<ContentRating> findByUser_IdAndContentIdAndContentType(
            Long userId, Long contentId, Content.ContentType contentType);
}
