package filmly.service.impl;

import filmly.dto.favoritegenres.FavoriteGenreDto;
import filmly.exception.EntityAlreadyExistsException;
import filmly.exception.EntityNotFoundException;
import filmly.model.FavoriteGenre;
import filmly.model.Genre;
import filmly.model.User;
import filmly.repository.FavoriteGenreRepository;
import filmly.repository.GenreRepository;
import filmly.repository.UserRepository;
import filmly.service.FavoriteGenreService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteGenreServiceImpl implements FavoriteGenreService {

    private static final float NEUTRAL_RATING = 5.0f;
    private final FavoriteGenreRepository favoriteGenresRepository;
    private final UserRepository userRepository;
    private final GenreRepository genreRepository;

    @Override
    public List<FavoriteGenre> getSortedFavoriteGenreByUserId(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User", email));
        List<FavoriteGenre> userFavoriteGenres = favoriteGenresRepository
                .findByUser_Id(user.getId());
        List<Genre> allGenres = genreRepository.findAll();
        List<FavoriteGenre> sortedGenres = new ArrayList<>(userFavoriteGenres);
        for (Genre genre : allGenres) {
            boolean isAlreadyFavorite = userFavoriteGenres.stream()
                    .anyMatch(favorite -> favorite.getGenre().getId().equals(genre.getId()));

            if (!isAlreadyFavorite) {
                FavoriteGenre defaultGenre = new FavoriteGenre();
                defaultGenre.setGenre(genre);
                defaultGenre.setRating((NEUTRAL_RATING));
                sortedGenres.add(defaultGenre);
            }
        }
        sortedGenres.sort((g1, g2)
                -> g2.getRating().compareTo(g1.getRating()));
        return sortedGenres;
    }

    @Override
    public List<FavoriteGenre> getAllByUserId(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User", email));
        return favoriteGenresRepository.findByUser_Id(user.getId());
    }

    @Override
    public FavoriteGenre createFavoriteGenre(String email, FavoriteGenreDto favoriteGenreDto) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User", email));
        Genre genre = genreRepository.findByName(favoriteGenreDto.genreName()).orElseThrow(
                () -> new EntityNotFoundException("Genre", user.getId()));
        if (favoriteGenresRepository.findByUser_IdAndGenre(user.getId(), genre).isPresent()) {
            throw new EntityAlreadyExistsException("You already have "
                    + favoriteGenreDto.genreName() + " in your favorites");
        }
        FavoriteGenre favoriteGenre = new FavoriteGenre();
        favoriteGenre.setUser(user);
        favoriteGenre.setGenre(genre);
        favoriteGenre.setRating(favoriteGenreDto.rating());
        return favoriteGenresRepository.save(favoriteGenre);
    }

    @Override
    public FavoriteGenre updateFavoriteGenre(String email, FavoriteGenreDto favoriteGenreDto) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User", email));
        Genre genre = genreRepository.findByName(favoriteGenreDto.genreName()).orElseThrow(
                () -> new EntityNotFoundException("Genre", user.getId()));
        FavoriteGenre favoriteGenre =
                favoriteGenresRepository.findByUser_IdAndGenre(user.getId(), genre)
                .orElseThrow(() -> new EntityNotFoundException("FavoriteGenre", user.getId()));
        favoriteGenre.setRating(favoriteGenreDto.rating());
        return favoriteGenresRepository.save(favoriteGenre);
    }

    @Override
    public void deleteFavoriteGenre(String email, String genreName) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User", email));
        Genre genre = genreRepository.findByName(genreName).orElseThrow(
                () -> new EntityNotFoundException("Genre", genreName)
        );
        favoriteGenresRepository.deleteByUserIdAndGenre(user.getId(), genre);
    }
}
