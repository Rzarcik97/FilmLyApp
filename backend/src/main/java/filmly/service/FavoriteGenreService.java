package filmly.service;

import filmly.dto.favoritegenres.FavoriteGenreDto;
import filmly.dto.favoritegenres.FavoriteGenreResponseDto;
import java.util.List;

public interface FavoriteGenreService {

    List<FavoriteGenreResponseDto> getSortedFavoriteGenreByUserId(String email);

    List<FavoriteGenreResponseDto> getAllByUserId(String email);

    FavoriteGenreResponseDto createFavoriteGenre(String email, FavoriteGenreDto favoriteGenreDto);

    FavoriteGenreResponseDto updateFavoriteGenre(String email, FavoriteGenreDto favoriteGenreDto);

    void deleteFavoriteGenre(String email, String genreName);
}
