package filmly.service;

import filmly.dto.favoritegenres.FavoriteGenreDto;
import filmly.model.FavoriteGenre;
import java.util.List;

public interface FavoriteGenreService {

    List<FavoriteGenre> getSortedFavoriteGenreByUserId(String email);

    List<FavoriteGenre> getAllByUserId(String email);

    FavoriteGenre createFavoriteGenre(String email, FavoriteGenreDto favoriteGenreDto);

    FavoriteGenre updateFavoriteGenre(String email, FavoriteGenreDto favoriteGenreDto);

    void deleteFavoriteGenre(String email, String genreName);
}
