package filmly.repository;

import filmly.model.FavoriteGenre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteGenreRepository extends JpaRepository<FavoriteGenre, Long> {
}
