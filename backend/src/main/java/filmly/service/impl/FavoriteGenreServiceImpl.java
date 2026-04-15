package filmly.service.impl;

import filmly.dto.favoritegenres.FavoriteGenreDto;
import filmly.dto.favoritegenres.FavoriteGenreResponseDto;
import filmly.exception.EntityAlreadyExistsException;
import filmly.exception.EntityNotFoundException;
import filmly.mapper.FavoriteGenreMapper;
import filmly.model.FavoriteGenre;
import filmly.model.Genre;
import filmly.model.User;
import filmly.repository.FavoriteGenreRepository;
import filmly.repository.GenreRepository;
import filmly.repository.UserRepository;
import filmly.service.FavoriteGenreService;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class FavoriteGenreServiceImpl implements FavoriteGenreService {

    private static final float NEUTRAL_RATING = 5.0f;
    private final FavoriteGenreRepository favoriteGenresRepository;
    private final UserRepository userRepository;
    private final GenreRepository genreRepository;
    private final FavoriteGenreMapper favoriteGenreMapper;

    @Override
    public List<FavoriteGenreResponseDto> getSortedFavoriteGenreByUserId(String email) {
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
        return favoriteGenreMapper.toDtoList(sortedGenres);
    }

    @Override
    public List<FavoriteGenreResponseDto> getAllByUserId(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User", email));
        return favoriteGenreMapper
                .toDtoList(favoriteGenresRepository.findByUser_Id(user.getId()));
    }

    @Override
    public FavoriteGenreResponseDto createFavoriteGenre(
            String email,
            FavoriteGenreDto favoriteGenreDto) {
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
        FavoriteGenre saved = favoriteGenresRepository.save(favoriteGenre);
        log.info("Created favorite genre '{}' for user {}", genre.getName(), email);
        return favoriteGenreMapper.toDto(saved);
    }

    @Override
    public FavoriteGenreResponseDto updateFavoriteGenre(
            String email,
            FavoriteGenreDto favoriteGenreDto) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User", email));
        Genre genre = genreRepository.findByName(favoriteGenreDto.genreName()).orElseThrow(
                () -> new EntityNotFoundException("Genre", user.getId()));
        FavoriteGenre favoriteGenre =
                favoriteGenresRepository.findByUser_IdAndGenre(user.getId(), genre)
                .orElseThrow(() -> new EntityNotFoundException("FavoriteGenre", user.getId()));
        favoriteGenre.setRating(favoriteGenreDto.rating());
        FavoriteGenre saved = favoriteGenresRepository.save(favoriteGenre);
        log.info("Updated favorite genre '{}' for user {}", genre.getName(), email);
        return favoriteGenreMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void deleteFavoriteGenre(String email, String genreName) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User", email));
        Genre genre = genreRepository.findByName(genreName).orElseThrow(
                () -> new EntityNotFoundException("Genre", genreName)
        );
        favoriteGenresRepository.deleteByUserIdAndGenre(user.getId(), genre);
        log.info("Deleted favorite genre '{}' for user {}", genre.getName(), email);
    }
}
