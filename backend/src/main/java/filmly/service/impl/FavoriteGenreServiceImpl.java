package filmly.service.impl;

import filmly.model.FavoriteGenre;
import filmly.service.FavoriteGenreService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteGenreServiceImpl implements FavoriteGenreService {
    // TODO implement logic, change return to DTO, do Exceptions
    @Override
    public FavoriteGenre getFavoriteGenreById(Long id) {
        return null;
    }

    @Override
    public List<FavoriteGenre> getAllByUserId(Long userId) {
        return List.of();
    }

    @Override
    public FavoriteGenre createFavoriteGenre(FavoriteGenre favoriteGenre) {
        return null;
    }

    @Override
    public FavoriteGenre updateFavoriteGenre(Long id, FavoriteGenre favoriteGenre) {
        return null;
    }

    @Override
    public void deleteFavoriteGenre(Long id) {

    }
}
