package filmly.service;

import filmly.dto.tmdb.TmdbContentResult;
import filmly.service.impl.RecommendationScorer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecommendationScorerTest {

    private RecommendationScorer recommendationScorer;

    @BeforeEach
    void setUp() {
        recommendationScorer = new RecommendationScorer();
    }

    @Test
    @DisplayName("""
            calculateGenreScore | should return positive score
            for liked genres
            """)
    void calculateGenreScore_positive() {
        // given
        List<Long> genres = List.of(1L, 2L);

        Map<Long, Double> ratings = Map.of(
                1L, 8.0,
                2L, 9.0
        );

        // when
        double result =
                recommendationScorer.calculateGenreScore(
                        genres,
                        ratings
                );

        // then
        assertTrue(result > 0);
    }

    @Test
    @DisplayName("""
            calculateGenreScore | should return negative score
            for disliked genres
            """)
    void calculateGenreScore_negative() {
        // given
        List<Long> genres = List.of(1L, 2L);

        Map<Long, Double> ratings = Map.of(
                1L, 1.0,
                2L, 2.0
        );

        // when
        double result =
                recommendationScorer.calculateGenreScore(
                        genres,
                        ratings
                );

        // then
        assertTrue(result < 0);
    }

    @Test
    @DisplayName("""
            calculateGenreScore | should return zero
            when all genres neutral
            """)
    void calculateGenreScore_neutral() {
        // given
        List<Long> genres = List.of(1L, 2L);

        Map<Long, Double> ratings = Map.of(
                1L, 5.0,
                2L, 5.0
        );

        // when
        double result =
                recommendationScorer.calculateGenreScore(
                        genres,
                        ratings
                );

        // then
        assertEquals(0.0, result);
    }

    @Test
    @DisplayName("""
            calculateVoteScore | should return zero
            when vote count below threshold
            """)
    void calculateVoteScore_belowThreshold() {
        // when
        double result =
                recommendationScorer.calculateVoteScore(
                        8.0,
                        40
                );

        // then
        assertEquals(0.0, result);
    }

    @Test
    @DisplayName("""
            calculateVoteScore | should return positive score
            """)
    void calculateVoteScore_positive() {
        // when
        double result =
                recommendationScorer.calculateVoteScore(
                        8.5,
                        500
                );

        // then
        assertTrue(result > 0);
    }

    @Test
    @DisplayName("""
            calculateReleaseScore | should return zero
            when release date empty
            """)
    void calculateReleaseScore_emptyDate() {
        // when
        double result =
                recommendationScorer.calculateReleaseScore(
                        "",
                        100
                );

        // then
        assertEquals(0.0, result);
    }

    @Test
    @DisplayName("""
            calculateReleaseScore | should return bonus
            for recent movie
            """)
    void calculateReleaseScore_recentMovie() {
        // given
        String recentDate =
                LocalDate.now().minusMonths(1).toString();

        // when
        double result =
                recommendationScorer.calculateReleaseScore(
                        recentDate,
                        100
                );

        // then
        assertEquals(10.0, result);
    }

    @Test
    @DisplayName("""
            calculateReleaseScore | should return lower bonus
            for old movie
            """)
    void calculateReleaseScore_oldMovie() {
        // given
        String oldDate =
                LocalDate.now().minusYears(20).toString();

        // when
        double result =
                recommendationScorer.calculateReleaseScore(
                        oldDate,
                        100
                );

        // then
        assertEquals(-5.0, result);
    }

    @Test
    @DisplayName("""
            calculatePopularityScore | should return positive score
            """)
    void calculatePopularityScore_success() {
        // when
        double result =
                recommendationScorer.calculatePopularityScore(100);

        // then
        assertTrue(result > 0);
    }

    @Test
    @DisplayName("""
            calculateFinalScore | should return combined score
            """)
    void calculateFinalScore_success() {
        // given
        TmdbContentResult movie = mock(TmdbContentResult.class);

        when(movie.genreIds())
                .thenReturn(List.of(1L, 2L));

        when(movie.voteAverage())
                .thenReturn(8.5);

        when(movie.voteCount())
                .thenReturn(500);

        when(movie.releaseDate())
                .thenReturn(
                        LocalDate.now().minusYears(1).toString()
                );

        when(movie.popularity())
                .thenReturn(100.0);

        when(movie.title())
                .thenReturn("Movie");

        Map<Long, Double> ratings = Map.of(
                1L, 8.0,
                2L, 9.0
        );

        // when
        double result =
                recommendationScorer.calculateFinalScore(
                        movie,
                        ratings
                );

        // then
        assertTrue(result > 0);
    }
}
