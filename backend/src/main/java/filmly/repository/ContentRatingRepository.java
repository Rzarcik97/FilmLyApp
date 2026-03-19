package filmly.repository;

import filmly.model.ContentRating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRatingRepository extends JpaRepository<ContentRating, Long> {
}
