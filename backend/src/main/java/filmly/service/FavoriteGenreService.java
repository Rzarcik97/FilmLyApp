package filmly.service;

import filmly.model.FavoriteGenre;
import java.util.List;

public interface FavoriteGenreService {

    FavoriteGenre getFavoriteGenreById(Long id);

    List<FavoriteGenre> getAllByUserId(Long userId);

    FavoriteGenre createFavoriteGenre(FavoriteGenre favoriteGenre);

    FavoriteGenre updateFavoriteGenre(Long id, FavoriteGenre favoriteGenre);

    void deleteFavoriteGenre(Long id);
}
