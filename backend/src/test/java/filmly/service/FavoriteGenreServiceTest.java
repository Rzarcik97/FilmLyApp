package filmly.service;

import filmly.dto.favoritegenres.FavoriteGenreDto;
import filmly.dto.favoritegenres.FavoriteGenreResponseDto;
import filmly.enums.GenreType;
import filmly.exception.EntityAlreadyExistsException;
import filmly.exception.EntityNotFoundException;
import filmly.mapper.FavoriteGenreMapper;
import filmly.model.FavoriteGenre;
import filmly.model.Genre;
import filmly.model.User;
import filmly.repository.FavoriteGenreRepository;
import filmly.repository.GenreRepository;
import filmly.repository.UserRepository;
import filmly.service.impl.FavoriteGenreServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FavoriteGenreServiceTest {

    @Mock
    private FavoriteGenreRepository favoriteGenresRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private FavoriteGenreMapper favoriteGenreMapper;

    @InjectMocks
    private FavoriteGenreServiceImpl favoriteGenreService;

    @Test
    @DisplayName("""
            getSortedFavoriteGenreByUserId | should return sorted genres
            with default genres added
            """)
    void getSortedFavoriteGenreByUserId_success() {
        // given
        String email = "john@example.com";

        User user = new User();
        user.setId(1L);

        Genre action = new Genre();
        action.setId(1L);
        action.setName("Action");

        Genre comedy = new Genre();
        comedy.setId(2L);
        comedy.setName("Comedy");

        FavoriteGenre favoriteGenre = new FavoriteGenre();
        favoriteGenre.setGenre(action);
        favoriteGenre.setRating(9.0f);

        FavoriteGenreResponseDto dto =
                mock(FavoriteGenreResponseDto.class);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(favoriteGenresRepository.findByUser_Id(1L))
                .thenReturn(List.of(favoriteGenre));

        when(genreRepository.findAll())
                .thenReturn(List.of(action, comedy));

        when(favoriteGenreMapper.toDtoList(anyList()))
                .thenReturn(List.of(dto));

        // when
        List<FavoriteGenreResponseDto> result =
                favoriteGenreService
                        .getSortedFavoriteGenreByUserId(email);

        // then
        assertEquals(1, result.size());

        verify(favoriteGenreMapper)
                .toDtoList(anyList());
    }

    @Test
    @DisplayName("""
            getAllByUserId | should return all favorite genres
            """)
    void getAllByUserId_success() {
        // given
        String email = "john@example.com";

        User user = new User();
        user.setId(1L);

        FavoriteGenre favoriteGenre = new FavoriteGenre();

        FavoriteGenreResponseDto dto =
                mock(FavoriteGenreResponseDto.class);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(favoriteGenresRepository.findByUser_Id(1L))
                .thenReturn(List.of(favoriteGenre));

        when(favoriteGenreMapper.toDtoList(List.of(favoriteGenre)))
                .thenReturn(List.of(dto));

        // when
        List<FavoriteGenreResponseDto> result =
                favoriteGenreService.getAllByUserId(email);

        // then
        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    @DisplayName("""
            createFavoriteGenre | should create favorite genre
            """)
    void createFavoriteGenre_success() {
        // given
        String email = "john@example.com";

        User user = new User();
        user.setId(1L);

        Genre genre = new Genre();
        genre.setId(10L);
        genre.setName("Action");

        FavoriteGenreDto dto =
                new FavoriteGenreDto("Action", 8.0f);

        FavoriteGenre saved = new FavoriteGenre();
        saved.setGenre(genre);

        FavoriteGenreResponseDto responseDto =
                mock(FavoriteGenreResponseDto.class);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(genreRepository.findByName("Action"))
                .thenReturn(Optional.of(genre));

        when(favoriteGenresRepository
                .findByUser_IdAndGenre(1L, genre))
                .thenReturn(Optional.empty());

        when(favoriteGenresRepository.save(any(FavoriteGenre.class)))
                .thenReturn(saved);

        when(favoriteGenreMapper.toDto(saved))
                .thenReturn(responseDto);

        // when
        FavoriteGenreResponseDto result =
                favoriteGenreService.createFavoriteGenre(email, dto);

        // then
        assertEquals(responseDto, result);

        verify(favoriteGenresRepository)
                .save(any(FavoriteGenre.class));
    }

    @Test
    @DisplayName("""
            createFavoriteGenre | should throw exception
            when genre already exists
            """)
    void createFavoriteGenre_alreadyExists() {
        // given
        String email = "john@example.com";

        User user = new User();
        user.setId(1L);

        Genre genre = new Genre();
        genre.setName("Action");

        FavoriteGenre existing = new FavoriteGenre();

        FavoriteGenreDto dto =
                new FavoriteGenreDto("Action", 8.0f);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(genreRepository.findByName("Action"))
                .thenReturn(Optional.of(genre));

        when(favoriteGenresRepository
                .findByUser_IdAndGenre(1L, genre))
                .thenReturn(Optional.of(existing));

        // when + then
        assertThrows(
                EntityAlreadyExistsException.class,
                () -> favoriteGenreService
                        .createFavoriteGenre(email, dto)
        );

        verify(favoriteGenresRepository, never())
                .save(any());
    }

    @Test
    @DisplayName("""
            updateFavoriteGenre | should update rating
            """)
    void updateFavoriteGenre_success() {
        // given
        String email = "john@example.com";

        User user = new User();
        user.setId(1L);

        Genre genre = new Genre();
        genre.setName("Action");

        FavoriteGenre favoriteGenre = new FavoriteGenre();
        favoriteGenre.setRating(5.0f);

        FavoriteGenreDto dto =
                new FavoriteGenreDto("Action", 9.0f);

        FavoriteGenreResponseDto responseDto =
                mock(FavoriteGenreResponseDto.class);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(genreRepository.findByName("Action"))
                .thenReturn(Optional.of(genre));

        when(favoriteGenresRepository
                .findByUser_IdAndGenre(1L, genre))
                .thenReturn(Optional.of(favoriteGenre));

        when(favoriteGenresRepository.save(favoriteGenre))
                .thenReturn(favoriteGenre);

        when(favoriteGenreMapper.toDto(favoriteGenre))
                .thenReturn(responseDto);

        // when
        FavoriteGenreResponseDto result =
                favoriteGenreService.updateFavoriteGenre(email, dto);

        // then
        assertEquals(9.0f, favoriteGenre.getRating());
        assertEquals(responseDto, result);

        verify(favoriteGenresRepository)
                .save(favoriteGenre);
    }

    @Test
    @DisplayName("""
        deleteFavoriteGenre | should delete favorite genre
        """)
    void deleteFavoriteGenre_success() {
        // given
        String email = "john@example.com";

        User user = new User();
        user.setId(1L);

        Genre genre = new Genre();
        genre.setName("Action");

        FavoriteGenre favoriteGenre = new FavoriteGenre();
        favoriteGenre.setGenre(genre);
        favoriteGenre.setUser(user);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(genreRepository.findByName("Action"))
                .thenReturn(Optional.of(genre));

        when(favoriteGenresRepository
                .findByUser_IdAndGenre(1L, genre))
                .thenReturn(Optional.of(favoriteGenre));

        // when
        favoriteGenreService
                .deleteFavoriteGenre(email, "Action");

        // then
        verify(favoriteGenresRepository)
                .findByUser_IdAndGenre(1L, genre);

        verify(favoriteGenresRepository)
                .delete(favoriteGenre);
    }

    @Test
    @DisplayName("""
            getUserGenreRatings | should return ratings map
            """)
    void getUserGenreRatings_success() {
        // given
        String email = "john@example.com";

        User user = new User();
        user.setId(1L);

        Genre genre = new Genre();
        genre.setId(10L);

        FavoriteGenre favoriteGenre = new FavoriteGenre();
        favoriteGenre.setGenre(genre);
        favoriteGenre.setRating(8.5f);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(favoriteGenresRepository.findByUser_Id(1L))
                .thenReturn(List.of(favoriteGenre));

        // when
        Map<Long, Double> result =
                favoriteGenreService.getUserGenreRatings(email);

        // then
        assertEquals(1, result.size());
        assertEquals(8.5, result.get(10L));
    }

    @Test
    @DisplayName("""
            getRandomGenreIds | should return max 4 ids
            """)
    void getRandomGenreIds_success() {
        // given
        List<Long> ids = List.of(1L, 2L, 3L, 4L, 5L, 6L);

        when(genreRepository.findAllGenreIdsByType(GenreType.MOVIE))
                .thenReturn(ids);

        // when
        List<Long> result =
                favoriteGenreService
                        .getRandomGenreIds(GenreType.MOVIE);

        // then
        assertEquals(4, result.size());
        assertTrue(ids.containsAll(result));
    }

    @Test
    @DisplayName("""
            createFavoriteGenre | should throw EntityNotFoundException
            when user not exists
            """)
    void createFavoriteGenre_userNotFound() {
        // given
        FavoriteGenreDto dto =
                new FavoriteGenreDto("Action", 8.0f);

        when(userRepository.findByEmail("john@example.com"))
                .thenReturn(Optional.empty());

        // when + then
        assertThrows(
                EntityNotFoundException.class,
                () -> favoriteGenreService
                        .createFavoriteGenre(
                                "john@example.com",
                                dto
                        )
        );

        verifyNoInteractions(
                genreRepository,
                favoriteGenresRepository
        );
    }

    @Test
    @DisplayName("""
        deleteFavoriteGenre | should do nothing
        when favorite genre does not exist
        """)
    void deleteFavoriteGenre_notFound() {
        // given
        String email = "john@example.com";

        User user = new User();
        user.setId(1L);

        Genre genre = new Genre();
        genre.setName("Action");

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(genreRepository.findByName("Action"))
                .thenReturn(Optional.of(genre));

        when(favoriteGenresRepository
                .findByUser_IdAndGenre(1L, genre))
                .thenReturn(Optional.empty());

        // when
        favoriteGenreService.deleteFavoriteGenre(email, "Action");

        // then
        verify(favoriteGenresRepository, never())
                .deleteByUserIdAndGenre(anyLong(), any());
    }
}
