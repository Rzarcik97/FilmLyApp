package filmly.service.impl;

import filmly.dto.tmdb.TmdbContentResult;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
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
    private static final double POPULARITY_WEIGHT = 0.5;
    private static final int VOTE_COUNT_THRESHOLD = 50;

    public double calculateFinalScore(
            TmdbContentResult movie,
            Map<Long, Double> userRatings
    ) {
        double genreScore = calculateGenreScore(movie.genreIds(), userRatings);
        double voteScore = calculateVoteScore(movie.voteAverage(), movie.voteCount());
        double releaseScore = calculateReleaseScore(movie.releaseDate(), movie.voteCount());
        double popularityScore = calculatePopularityScore(movie.popularity());

        double finalScore = genreScore
                + voteScore
                + releaseScore
                + popularityScore;

        log.debug("[{}] genre={} vote={} release={} popularity={} final={}",
                movie.title() != null ? movie.title() : movie.name(),
                String.format("%.2f", genreScore),
                String.format("%.2f", voteScore),
                String.format("%.2f", releaseScore),
                String.format("%.2f", popularityScore),
                String.format("%.2f", finalScore));

        return finalScore;
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

        return avg * multiplier * GENRE_WEIGHT;
    }

    public double calculateVoteScore(double voteAverage, int voteCount) {
        if (voteCount < VOTE_COUNT_THRESHOLD) {
            return 0.0;
        }

        double qualityScore = (voteAverage - 5.0)
                * Math.log10(voteCount - VOTE_COUNT_THRESHOLD - 1);

        return Math.clamp(qualityScore, -20.0, 20.0) * VOTE_WEIGHT;
    }

    public double calculateReleaseScore(String releaseDate, int voteCount) {
        if (releaseDate == null || releaseDate.isBlank()) {
            return 0.0;
        }

        LocalDate now = LocalDate.now();
        long monthsAgo = ChronoUnit.MONTHS.between(LocalDate.parse(releaseDate), now);
        long yearsAgo = ChronoUnit.YEARS.between(LocalDate.parse(releaseDate), now);

        if (monthsAgo < 3) {
            return voteCount < VOTE_COUNT_THRESHOLD ? 5.0 : 10.0;
        }

        return mapAgeToReleaseBonus(yearsAgo) * RELEASE_WEIGHT;
    }

    public double calculatePopularityScore(double popularity) {
        // return Math.log10(popularity + 1);
        return Math.sqrt(popularity) * POPULARITY_WEIGHT;
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
