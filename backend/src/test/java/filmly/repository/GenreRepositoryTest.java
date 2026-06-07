package filmly.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import filmly.enums.GenreType;
import filmly.model.Genre;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    // ── findByName ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("""
            findByName | Returns genre when name exists
            """)
    void findByName_GenreExists_ShouldReturnGenre() {
        // When
        Optional<Genre> result = genreRepository.findByName("Action");

        // Then
        assertTrue(result.isPresent());
        assertEquals("Action", result.get().getName());
        assertEquals(GenreType.MOVIE, result.get().getType());
    }

    @Test
    @DisplayName("""
            findByName | Returns empty when name does not exist
            """)
    void findByName_GenreNotExists_ShouldReturnEmpty() {
        // When
        Optional<Genre> result = genreRepository.findByName("NonExistentGenre");

        // Then
        assertTrue(result.isEmpty());
    }

    // ── findAllByNameIn ───────────────────────────────────────────────────────

    @Test
    @DisplayName("""
            findAllByNameIn | Returns genres matching given names
            """)
    void findAllByNameIn_ShouldReturnMatchingGenres() {
        // When
        List<Genre> result = genreRepository.findAllByNameIn(List.of("Action", "Drama", "Comedy"));

        // Then
        assertEquals(3, result.size());
        assertTrue(result.stream().anyMatch(g -> g.getName().equals("Action")));
        assertTrue(result.stream().anyMatch(g -> g.getName().equals("Drama")));
        assertTrue(result.stream().anyMatch(g -> g.getName().equals("Comedy")));
    }

    @Test
    @DisplayName("""
            findAllByNameIn | Returns empty list when no names match
            """)
    void findAllByNameIn_NoMatches_ShouldReturnEmpty() {
        // When
        List<Genre> result = genreRepository.findAllByNameIn(
                List.of("NonExistent1", "NonExistent2"));

        // Then
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("""
            findAllByNameIn | Returns partial matches when only some names exist
            """)
    void findAllByNameIn_PartialMatch_ShouldReturnOnlyExisting() {
        // When
        List<Genre> result = genreRepository.findAllByNameIn(
                List.of("Action", "NonExistentGenre"));

        // Then
        assertEquals(1, result.size());
        assertEquals("Action", result.getFirst().getName());
    }

    // ── findAllGenreIdsByType ─────────────────────────────────────────────────

    @Test
    @DisplayName("""
            findAllGenreIdsByType | Returns MOVIE and BOTH genre ids for MOVIE type
            """)
    void findAllGenreIdsByType_MovieType_ShouldReturnMovieAndBothGenres() {
        // When
        List<Long> result = genreRepository.findAllGenreIdsByType(GenreType.MOVIE);

        // Then
        assertFalse(result.isEmpty());
        // Action (id=28) is MOVIE type — should be included
        assertTrue(result.contains(28L));
        // Drama (id=18) is BOTH type — should be included
        assertTrue(result.contains(18L));
        // Action & Adventure (id=10759) is SERIES type — should NOT be included
        assertFalse(result.contains(10759L));
    }

    @Test
    @DisplayName("""
            findAllGenreIdsByType | Returns SERIES and BOTH genre ids for SERIES type
            """)
    void findAllGenreIdsByType_SeriesType_ShouldReturnSeriesAndBothGenres() {
        // When
        List<Long> result = genreRepository.findAllGenreIdsByType(GenreType.SERIES);

        // Then
        assertFalse(result.isEmpty());
        // Action & Adventure (id=10759) is SERIES type — should be included
        assertTrue(result.contains(10759L));
        // Drama (id=18) is BOTH type — should be included
        assertTrue(result.contains(18L));
        // Action (id=28) is MOVIE type — should NOT be included
        assertFalse(result.contains(28L));
    }
}
