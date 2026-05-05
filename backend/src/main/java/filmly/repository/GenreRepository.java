package filmly.repository;

import filmly.model.Genre;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findByName(String name);

    List<Genre> findAllByNameIn(Collection<String> names);

    @Query("SELECT g.id FROM Genre g WHERE g.type IN ('MOVIE', 'BOTH') ORDER BY RAND() LIMIT 4")
    List<Long> findRandomMovieGenreIds();
}
