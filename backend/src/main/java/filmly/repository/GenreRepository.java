package filmly.repository;

import filmly.enums.GenreType;
import filmly.model.Genre;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findByName(String name);

    List<Genre> findAllByNameIn(Collection<String> names);

    @Query("SELECT g.id FROM Genre g WHERE g.type = :type OR g.type = 'BOTH'")
    List<Long> findAllGenreIdsByType(@Param("type") GenreType type);
}
