package filmly.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.List;

import filmly.model.Content;
import filmly.model.User;
import filmly.model.WatchList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class WatchListRepositoryTest {

    @Autowired
    private WatchListRepository watchListRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContentRepository contentRepository;

    private User john;
    private User jane;
    private Content movie1;
    private Content movie2;

    @BeforeEach
    void setUp() {
        watchListRepository.deleteAll();
        john = userRepository.findByEmail("john@test.com").orElseThrow();
        jane = userRepository.findByEmail("jane@test.com").orElseThrow();
        movie1 = contentRepository.findByExternalIdAndType(1630423L, Content.ContentType.MOVIE)
                .orElseThrow();
        movie2 = contentRepository.findByExternalIdAndType(1007757L, Content.ContentType.MOVIE)
                .orElseThrow();
    }

    private WatchList buildWatchList(User user, Content content, boolean watched) {
        WatchList watchList = new WatchList();
        watchList.setUser(user);
        watchList.setContent(content);
        watchList.setAddedAt(LocalDateTime.now());
        if (watched) {
            watchList.setWatchedAt(LocalDateTime.now());
        }
        return watchList;
    }

    // ── findByUser_IdAndContent_Type ──────────────────────────────────────────

    @Test
    @DisplayName("""
            findByUser_IdAndContent_Type | Returns all watchlist entries for user and type
            """)
    void findByUser_IdAndContent_Type_ShouldReturnAllEntries() {
        // Given
        watchListRepository.save(buildWatchList(john, movie1, false));
        watchListRepository.save(buildWatchList(john, movie2, true));

        // When
        List<WatchList> result = watchListRepository
                .findByUser_IdAndContent_Type(john.getId(), Content.ContentType.MOVIE);

        // Then
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("""
            findByUser_IdAndContent_Type | Returns empty list when user has no watchlist entries
            """)
    void findByUser_IdAndContent_Type_NoEntries_ShouldReturnEmpty() {
        // When
        List<WatchList> result = watchListRepository
                .findByUser_IdAndContent_Type(john.getId(), Content.ContentType.MOVIE);

        // Then
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("""
            findByUser_IdAndContent_Type | Does not return entries for other users
            """)
    void findByUser_IdAndContent_Type_OtherUser_ShouldNotBeReturned() {
        // Given
        watchListRepository.save(buildWatchList(john, movie1, false));
        watchListRepository.save(buildWatchList(jane, movie2, false));

        // When
        List<WatchList> result = watchListRepository
                .findByUser_IdAndContent_Type(john.getId(), Content.ContentType.MOVIE);

        // Then
        assertEquals(1, result.size());
        assertEquals(movie1.getExternalId(), result.get(0).getContent().getExternalId());
    }

    @Test
    @DisplayName("""
            findByUser_IdAndContent_Type | Genres are eagerly fetched
            """)
    void findByUser_IdAndContent_Type_ShouldFetchGenres() {
        // Given
        watchListRepository.save(buildWatchList(john, movie1, false));

        // When
        List<WatchList> result = watchListRepository
                .findByUser_IdAndContent_Type(john.getId(), Content.ContentType.MOVIE);

        // Then
        assertNotNull(result.get(0).getContent().getGenres());
        assertFalse(result.get(0).getContent().getGenres().isEmpty());
    }

    // ── findByUser_IdAndWatchedAtIsNullAndContent_Type ────────────────────────

    @Test
    @DisplayName("""
            findByUser_IdAndWatchedAtIsNullAndContent_Type | Returns only unwatched entries
            """)
    void findByUser_IdAndWatchedAtIsNullAndContent_Type_ShouldReturnOnlyUnwatched() {
        // Given
        watchListRepository.save(buildWatchList(john, movie1, false));
        watchListRepository.save(buildWatchList(john, movie2, true));

        // When
        List<WatchList> result = watchListRepository
                .findByUser_IdAndWatchedAtIsNullAndContent_Type(
                        john.getId(), Content.ContentType.MOVIE);

        // Then
        assertEquals(1, result.size());
        assertEquals(movie1.getExternalId(), result.get(0).getContent().getExternalId());
    }

    @Test
    @DisplayName("""
            findByUser_IdAndWatchedAtIsNullAndContent_Type | Returns empty when all watched
            """)
    void findByUser_IdAndWatchedAtIsNullAndContent_Type_AllWatched_ShouldReturnEmpty() {
        // Given
        watchListRepository.save(buildWatchList(john, movie1, true));
        watchListRepository.save(buildWatchList(john, movie2, true));

        // When
        List<WatchList> result = watchListRepository
                .findByUser_IdAndWatchedAtIsNullAndContent_Type(
                        john.getId(), Content.ContentType.MOVIE);

        // Then
        assertEquals(0, result.size());
    }
}
