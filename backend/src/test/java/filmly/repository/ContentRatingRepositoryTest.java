package filmly.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import filmly.model.Content;
import filmly.model.ContentRating;
import filmly.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class ContentRatingRepositoryTest {

    @Autowired
    private ContentRatingRepository contentRatingRepository;

    @Autowired
    private UserRepository userRepository;

    private User john;
    private User jane;

    @BeforeEach
    void setUp() {
        contentRatingRepository.deleteAll();
        john = userRepository.findByEmail("john@test.com").orElseThrow();
        jane = userRepository.findByEmail("jane@test.com").orElseThrow();
    }

    private ContentRating buildRating(User user, Long contentId,
                                      Content.ContentType type, Float rating, String review) {
        ContentRating cr = new ContentRating();
        cr.setUser(user);
        cr.setContentId(contentId);
        cr.setContentType(type);
        cr.setRating(rating);
        cr.setReview(review);
        cr.setCreatedAt(LocalDateTime.now());
        return cr;
    }

    // ── findByUser_IdAndContentIdAndContentType ───────────────────────────────

    @Test
    @DisplayName("""
            findByUser_IdAndContentIdAndContentType | Returns rating when exists
            """)
    void findByUser_IdAndContentIdAndContentType_ShouldReturnRating() {
        // Given
        contentRatingRepository.save(
                buildRating(john, 1630423L, Content.ContentType.MOVIE, 8.0f, "Great movie!"));

        // When
        Optional<ContentRating> result = contentRatingRepository
                .findByUser_IdAndContentIdAndContentType(
                        john.getId(), 1630423L, Content.ContentType.MOVIE);

        // Then
        assertTrue(result.isPresent());
        assertEquals(8.0f, result.get().getRating());
        assertEquals("Great movie!", result.get().getReview());
    }

    @Test
    @DisplayName("""
            findByUser_IdAndContentIdAndContentType | Returns empty when not exists
            """)
    void findByUser_IdAndContentIdAndContentType_NotExists_ShouldReturnEmpty() {
        // When
        Optional<ContentRating> result = contentRatingRepository
                .findByUser_IdAndContentIdAndContentType(
                        john.getId(), 1630423L, Content.ContentType.MOVIE);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("""
            findByUser_IdAndContentIdAndContentType | Returns empty for different user
            """)
    void findByUser_IdAndContentIdAndContentType_DifferentUser_ShouldReturnEmpty() {
        // Given
        contentRatingRepository.save(
                buildRating(john, 1630423L, Content.ContentType.MOVIE, 8.0f, null));

        // When
        Optional<ContentRating> result = contentRatingRepository
                .findByUser_IdAndContentIdAndContentType(
                        jane.getId(), 1630423L, Content.ContentType.MOVIE);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("""
            findByUser_IdAndContentIdAndContentType | Returns empty for different content type
            """)
    void findByUser_IdAndContentIdAndContentType_DifferentType_ShouldReturnEmpty() {
        // Given
        contentRatingRepository.save(
                buildRating(john, 1630423L, Content.ContentType.MOVIE, 8.0f, null));

        // When
        Optional<ContentRating> result = contentRatingRepository
                .findByUser_IdAndContentIdAndContentType(
                        john.getId(), 1630423L, Content.ContentType.SERIES);

        // Then
        assertTrue(result.isEmpty());
    }

    // ── findLatestByContentIdAndContentType ───────────────────────────────────

    @Test
    @DisplayName("""
            findLatestByContentIdAndContentType | Returns latest 10 ratings ordered by date
            """)
    void findLatestByContentIdAndContentType_ShouldReturnLatestRatings() {
        // Given — insert 3 ratings with different timestamps
        ContentRating r1 = buildRating(john, 1630423L, Content.ContentType.MOVIE, 7.0f, "Good");
        r1.setCreatedAt(LocalDateTime.now().minusDays(2));

        ContentRating r2 = buildRating(jane, 1630423L, Content.ContentType.MOVIE, 9.0f, "Excellent");
        r2.setCreatedAt(LocalDateTime.now().minusDays(1));

        contentRatingRepository.saveAll(List.of(r1, r2));

        // When
        List<ContentRating> result = contentRatingRepository
                .findLatestByContentIdAndContentType(1630423L, Content.ContentType.MOVIE);

        // Then
        assertEquals(2, result.size());
        assertEquals(9.0f, result.get(0).getRating()); // newest first
        assertEquals(7.0f, result.get(1).getRating());
    }

    @Test
    @DisplayName("""
            findLatestByContentIdAndContentType | Returns empty list when no ratings exist
            """)
    void findLatestByContentIdAndContentType_NoRatings_ShouldReturnEmpty() {
        // When
        List<ContentRating> result = contentRatingRepository
                .findLatestByContentIdAndContentType(999L, Content.ContentType.MOVIE);

        // Then
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("""
            findLatestByContentIdAndContentType | Returns max 10 ratings
            """)
    void findLatestByContentIdAndContentType_MoreThan10_ShouldReturnOnly10() {
        // Given — need 11 users so reuse john and jane with different contentIds
        // instead insert 11 ratings from different content ids won't work
        // so we create multiple users via repo directly
        for (int i = 0; i < 11; i++) {
            User user = new User();
            user.setUsername("user" + i);
            user.setEmail("user" + i + "@test.com");
            user.setPassword("pass");
            user.setFirstName("User");
            user.setLastName(String.valueOf(i));
            user.setAvatarUrl("/avatar.jpg");
            user.setCreatedAt(LocalDateTime.now());
            user = userRepository.save(user);

            contentRatingRepository.save(
                    buildRating(user, 1630423L, Content.ContentType.MOVIE,
                            (float) ((i % 10) + 1), "Review " + i));
        }

        // When
        List<ContentRating> result = contentRatingRepository
                .findLatestByContentIdAndContentType(1630423L, Content.ContentType.MOVIE);

        // Then
        assertEquals(10, result.size());
    }
}
