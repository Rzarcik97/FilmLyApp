package filmly.repository;

import filmly.model.Content;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ContentRepository extends JpaRepository<Content, Long> {

    @Query("SELECT c FROM Content c "
            + "LEFT JOIN FETCH c.genres "
            + "WHERE c.externalId = :externalId "
            + "AND c.type = :type")
    Optional<Content> findByExternalIdAndType(Long externalId, Content.ContentType type);
}
