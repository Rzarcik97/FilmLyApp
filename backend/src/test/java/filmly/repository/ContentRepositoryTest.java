package filmly.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import filmly.model.Content;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class ContentRepositoryTest {

    @Autowired
    private ContentRepository contentRepository;

    // ── findByExternalIdAndType ───────────────────────────────────────────────

    @Test
    @DisplayName("""
            findByExternalIdAndType | Returns content when exists
            """)
    void findByExternalIdAndType_ContentExists_ShouldReturnContent() {
        // When
        Optional<Content> result = contentRepository
                .findByExternalIdAndType(1630423L, Content.ContentType.MOVIE);

        // Then
        assertTrue(result.isPresent());
        assertEquals(1630423L, result.get().getExternalId());
        assertEquals(Content.ContentType.MOVIE, result.get().getType());
        assertEquals("My Dearest Assassin", result.get().getTitle());
    }

    @Test
    @DisplayName("""
            findByExternalIdAndType | Returns empty when externalId does not exist
            """)
    void findByExternalIdAndType_NotExists_ShouldReturnEmpty() {
        // When
        Optional<Content> result = contentRepository
                .findByExternalIdAndType(999999L, Content.ContentType.MOVIE);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("""
            findByExternalIdAndType | Returns empty when type does not match
            """)
    void findByExternalIdAndType_WrongType_ShouldReturnEmpty() {
        // When — movie exists but queried as SERIES
        Optional<Content> result = contentRepository
                .findByExternalIdAndType(1630423L, Content.ContentType.SERIES);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("""
            findByExternalIdAndType | Genres are eagerly fetched
            """)
    void findByExternalIdAndType_ShouldFetchGenres() {
        // When
        Optional<Content> result = contentRepository
                .findByExternalIdAndType(1630423L, Content.ContentType.MOVIE);

        // Then
        assertTrue(result.isPresent());
        assertFalse(result.get().getGenres().isEmpty());
        assertTrue(result.get().getGenres().stream()
                .anyMatch(g -> g.getName().equals("Action")));
    }
}
