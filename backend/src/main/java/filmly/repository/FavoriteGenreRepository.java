package filmly.repository;

import filmly.model.FavoriteGenre;
import filmly.model.Genre;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteGenreRepository extends JpaRepository<FavoriteGenre, Long> {

    List<FavoriteGenre> findByUser_Id(Long userId);

    void deleteByUserIdAndGenre(Long userId, Genre genre);

    Optional<FavoriteGenre> findByUser_IdAndGenre(Long userId, Genre genre);
}
