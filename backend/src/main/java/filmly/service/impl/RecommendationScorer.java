package filmly.service.impl;

import filmly.dto.tmdb.TmdbContentResult;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecommendationScorer {
    /**
     * This value controls how much we reward movies for matching multiple genres
     * the user likes.
     * More matching genres = better score, but each next genre matters a bit less
     * (diminishing returns using a logarithm).
     * Recommended range:
     * - 0.10 → very subtle effect (almost no impact)
     * - 0.25 → balanced (recommended default)
     * - 0.40 → strong effect (genre variety matters a lot)
     */
    private static final double GENRE_MATCH_MULTIPLIER_FACTOR = 0.25;

    private static final double VOTE_WEIGHT = 1.0;
    private static final double GENRE_WEIGHT = 3.0;
    private static final double RELEASE_WEIGHT = 1.0;
    private static final double POPULARITY_WEIGHT = 2.0;

    public double calculateFinalScore(
            TmdbContentResult movie,
            Map<Long, Double> userRatings
    ) {
        double genreScore = calculateGenreScore(movie.genreIds(), userRatings);
        double voteScore = calculateVoteScore(movie.voteAverage(), movie.voteCount());
        double releaseScore = calculateReleaseScore(movie.releaseDate(), movie.voteCount());
        double popularityScore = calculatePopularityScore(movie.popularity());

        return genreScore * GENRE_WEIGHT
                + voteScore * VOTE_WEIGHT
                + releaseScore * RELEASE_WEIGHT
                + popularityScore * POPULARITY_WEIGHT;
    }

    public double calculateGenreScore(
            List<Long> movieGenres,
            Map<Long, Double> userRatings
    ) {
        double sumPoints = 0.0;
        int countedGenres = 0;
        int positiveGenres = 0;

        for (Long genreId : movieGenres) {
            double rating = userRatings.getOrDefault(genreId, 5.0);

            double points = mapRatingToPoints(rating);

            if (points != 0) {
                sumPoints += points;
                countedGenres++;
            }

            if (rating >= 6.0) {
                positiveGenres++;
            }
        }

        if (countedGenres == 0) {
            return 0.0;
        }

        double avg = sumPoints / countedGenres;

        double multiplier = 1.0 + GENRE_MATCH_MULTIPLIER_FACTOR
                * (Math.log(positiveGenres + 1) / Math.log(2));

        return avg * multiplier;
    }

    public double calculateVoteScore(double voteAverage, int voteCount) {
        if (voteCount < 50) {
            return 0.0;
        }

        double qualityScore = (voteAverage - 5.0) * Math.log10(voteCount - 49);

        return Math.clamp(qualityScore, -20.0, 20.0);
    }

    public double calculateReleaseScore(String releaseDate, int voteCount) {
        if (releaseDate == null || releaseDate.isBlank()) {
            return 0.0;
        }

        LocalDate now = LocalDate.now();
        long monthsAgo = ChronoUnit.MONTHS.between(LocalDate.parse(releaseDate), now);
        long yearsAgo = ChronoUnit.YEARS.between(LocalDate.parse(releaseDate), now);

        if (monthsAgo < 3) {
            return voteCount < 50 ? 5.0 : 10.0;
        }

        return mapAgeToReleaseBonus(yearsAgo);
    }

    public double calculatePopularityScore(double popularity) {
        return Math.log10(popularity + 1);
    }

    private double mapRatingToPoints(double rating) {
        if (rating < 2.0) return -4;
        if (rating < 3.0) return -3;
        if (rating < 4.0) return -2;
        if (rating < 5.0) return -1;
        if (rating == 5.0) return 0;
        if (rating < 6.0) return 1;
        if (rating < 7.0) return 2;
        if (rating < 8.0) return 3;
        if (rating < 9.0) return 4;
        return 5;
    }

    private double mapAgeToReleaseBonus(long yearsAgo) {
        if (yearsAgo < 1) return 5.0;
        if (yearsAgo < 2) return 3.0;
        if (yearsAgo < 5) return 1.0;
        if (yearsAgo < 8) return 0.0;
        if (yearsAgo < 15) return -3.0;
        if (yearsAgo < 25) return -5.0;
        return -10.0;
    }
}
