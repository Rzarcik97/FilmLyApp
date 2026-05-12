package filmly.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import filmly.model.Content;
import filmly.model.ContentLike;
import filmly.model.User;
import filmly.dto.contentlikes.ContentLikeCountProjection;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Log4j2
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ContentLikeRepositoryTest {

    @Autowired
    private ContentLikeRepository contentLikeRepository;

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

        contentLikeRepository.deleteAll();

        john = userRepository.findByEmail("john@test.com").orElseThrow();
        jane = userRepository.findByEmail("jane@test.com").orElseThrow();

        movie1 = contentRepository.findByExternalIdAndType(1630423L, Content.ContentType.MOVIE)
                .orElseThrow();
        movie2 = contentRepository.findByExternalIdAndType(1007757L, Content.ContentType.MOVIE)
                .orElseThrow();
    }

    private ContentLike buildLike(User user, Content content, Boolean liked) {
        ContentLike like = new ContentLike();
        like.setUser(user);
        like.setContent(content);
        like.setLiked(liked);
        like.setCreatedAt(LocalDateTime.now());
        return like;
    }

    // ── findByUser_IdAndContent_TypeAndLiked ──────────────────────────────────

    @Test
    @DisplayName("""
            findByUser_IdAndContent_TypeAndLiked | Returns liked content for user
            """)
    void findByUser_IdAndContent_TypeAndLiked_ShouldReturnLikedContent() {
        // Given
        contentLikeRepository.save(buildLike(john, movie1, true));
        contentLikeRepository.save(buildLike(john, movie2, false));

        // When
        List<ContentLike> result = contentLikeRepository
                .findByUser_IdAndContent_TypeAndLiked(
                        john.getId(), Content.ContentType.MOVIE, true);

        // Then
        assertEquals(1, result.size());
        assertEquals(movie1.getExternalId(), result.getFirst().getContent().getExternalId());
        assertTrue(result.getFirst().getLiked());
    }

    @Test
    @DisplayName("""
            findByUser_IdAndContent_TypeAndLiked | Returns disliked content for user
            """)
    void findByUser_IdAndContent_TypeAndLiked_ShouldReturnDislikedContent() {
        // Given
        contentLikeRepository.save(buildLike(john, movie1, true));
        contentLikeRepository.save(buildLike(john, movie2, false));

        // When
        List<ContentLike> result = contentLikeRepository
                .findByUser_IdAndContent_TypeAndLiked(
                        john.getId(), Content.ContentType.MOVIE, false);

        // Then
        assertEquals(1, result.size());
        assertEquals(movie2.getExternalId(), result.getFirst().getContent().getExternalId());
        assertFalse(result.getFirst().getLiked());
    }

    @Test
    @DisplayName("""
            findByUser_IdAndContent_TypeAndLiked | Returns empty list when user has no likes
            """)
    void findByUser_IdAndContent_TypeAndLiked_NoLikes_ShouldReturnEmpty() {
        // When
        List<ContentLike> result = contentLikeRepository
                .findByUser_IdAndContent_TypeAndLiked(
                        john.getId(), Content.ContentType.MOVIE, true);

        // Then
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("""
            findByUser_IdAndContent_TypeAndLiked | Genres are eagerly fetched
            """)
    void findByUser_IdAndContent_TypeAndLiked_ShouldFetchGenres() {
        // Given
        contentLikeRepository.save(buildLike(john, movie1, true));

        // When
        List<ContentLike> result = contentLikeRepository
                .findByUser_IdAndContent_TypeAndLiked(
                        john.getId(), Content.ContentType.MOVIE, true);

        // Then
        assertNotNull(result.getFirst().getContent().getGenres());
        assertFalse(result.getFirst().getContent().getGenres().isEmpty());
    }

    // ── countLikesAndDislikesByContentIds ─────────────────────────────────────

    @Test
    @DisplayName("""
            countLikesAndDislikesByContentIds | Returns correct counts for multiple movies
            """)
    void countLikesAndDislikesByContentIds_ShouldReturnCorrectCounts() {
        // Given
        contentLikeRepository.save(buildLike(john, movie1, true));
        contentLikeRepository.save(buildLike(jane, movie1, true));
        contentLikeRepository.save(buildLike(john, movie2, false));

        // When
        List<ContentLikeCountProjection> result = contentLikeRepository
                .countLikesAndDislikesByContentIds(
                        List.of(movie1.getExternalId(), movie2.getExternalId()),
                        Content.ContentType.MOVIE);

        // Then
        assertEquals(2, result.size());

        ContentLikeCountProjection movie1Stats = result.stream()
                .filter(p -> p.getContentId().equals(movie1.getExternalId()))
                .findFirst().orElseThrow();
        assertEquals(2L, movie1Stats.getLikes());
        assertEquals(0L, movie1Stats.getDislikes());

        ContentLikeCountProjection movie2Stats = result.stream()
                .filter(p -> p.getContentId().equals(movie2.getExternalId()))
                .findFirst().orElseThrow();
        assertEquals(0L, movie2Stats.getLikes());
        assertEquals(1L, movie2Stats.getDislikes());
    }

    @Test
    @DisplayName("""
            countLikesAndDislikesByContentIds | Returns empty list when no likes exist
            """)
    void countLikesAndDislikesByContentIds_NoLikes_ShouldReturnEmpty() {
        // When
        List<ContentLikeCountProjection> result = contentLikeRepository
                .countLikesAndDislikesByContentIds(
                        List.of(movie1.getExternalId()),
                        Content.ContentType.MOVIE);

        // Then
        assertEquals(0, result.size());
    }

    // ── countByContent_ExternalIdAndContent_TypeAndLiked ─────────────────────

    @Test
    @DisplayName("""
            countByContent_ExternalIdAndContent_TypeAndLiked | Returns correct like count
            """)
    void countByContent_ExternalIdAndContent_TypeAndLiked_ShouldReturnCount() {
        // Given
        contentLikeRepository.save(buildLike(john, movie1, true));
        contentLikeRepository.save(buildLike(jane, movie1, true));

        // When
        Long likes = contentLikeRepository
                .countByContent_ExternalIdAndContent_TypeAndLiked(
                        movie1.getExternalId(), Content.ContentType.MOVIE, true);

        Long dislikes = contentLikeRepository
                .countByContent_ExternalIdAndContent_TypeAndLiked(
                        movie1.getExternalId(), Content.ContentType.MOVIE, false);

        // Then
        assertEquals(2L, likes);
        assertEquals(0L, dislikes);
    }

    // ── findByUser_IdAndContent_ExternalIdAndContent_Type ────────────────────

    @Test
    @DisplayName("""
            findByUser_IdAndContent_ExternalIdAndContent_Type | Returns like when exists
            """)
    void findByUser_IdAndContent_ExternalIdAndContent_Type_ShouldReturnLike() {
        // Given
        contentLikeRepository.save(buildLike(john, movie1, true));

        // When
        Optional<ContentLike> result = contentLikeRepository
                .findByUser_IdAndContent_ExternalIdAndContent_Type(
                        john.getId(), movie1.getExternalId(), Content.ContentType.MOVIE);

        // Then
        assertTrue(result.isPresent());
        assertTrue(result.get().getLiked());
    }

    @Test
    @DisplayName("""
            findByUser_IdAndContent_ExternalIdAndContent_Type | Returns empty when not exists
            """)
    void findByUser_IdAndContent_ExternalIdAndContent_Type_ShouldReturnEmpty() {
        // When
        Optional<ContentLike> result = contentLikeRepository
                .findByUser_IdAndContent_ExternalIdAndContent_Type(
                        john.getId(), movie1.getExternalId(), Content.ContentType.MOVIE);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("""
            findByUser_IdAndContent_ExternalIdAndContent_Type | Returns empty for different user
            """)
    void findByUser_IdAndContent_ExternalIdAndContent_Type_DifferentUser_ShouldReturnEmpty() {
        // Given
        contentLikeRepository.save(buildLike(john, movie1, true));

        // When
        Optional<ContentLike> result = contentLikeRepository
                .findByUser_IdAndContent_ExternalIdAndContent_Type(
                        jane.getId(), movie1.getExternalId(), Content.ContentType.MOVIE);

        // Then
        assertTrue(result.isEmpty());
    }
}
