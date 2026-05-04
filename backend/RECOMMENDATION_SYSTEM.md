# Recommendation System Plan

## Overview
Score-based recommendation system that combines user preferences with content quality.
Runs on-the-fly without storing results in the database — can be optimized later with caching (Redis) or a `recommendation_scores` table.

---

## Flow

1. Calculate user's genre weights
2. Take top 5 genres with the highest weight
3. Fetch 3 pages from TMDB per genre (`/discover`) → ~300 movies/series
4. Deduplicate by `contentId`
5. Calculate `final_score` for each movie/series
6. Return top 20

---

## Recommendation Weight Sources

| Source                 | Weight                                     |
|------------------------|--------------------------------------------|
| `VoteScore`            | interval between (MIN -20 pkt, MAX 20 pkt) |
| `FavoriteGenreScore`   | interval between (MIN -12 pkt, MAX 20 pkt) |
| `RealeseScore`         | interval between (MIN -10 pkt, MAX 10 pkt) |
| `TMDDBPopularityScore` | interval between (MIN 0 pkt, MAX 10 pkt)   |

### vote_Rating - quality_score
Combines `vote_average` and `vote_count` into a single multiplier:

```
vote_count < 50   → quality_score = 0 (ignored)
vote_count >= 50  → quality_score = (vote_average - 5.0) × log10(vote_count - 49)
```

**Behavior:**
- `vote_average < 5.0` → subtracts points (bad movie)
- `vote_average = 5.0` → neutral (0 pts)
- `vote_average > 5.0` → adds points
- Low vote count → logarithm close to 0, low impact
- High vote count → logarithm grows, higher impact

**Examples:**

| vote_average | vote_count | quality_score                          |
|--------------|------------|----------------------------------------|
| 4.0          | 1000       | `(4.0 - 5.0) × log10(951)` = **-3.0**  |
| 5.0          | 1000       | `(5.0 - 5.0) × log10(951)` = **0.0**   |
| 7.2          | 50         | `(7.2 - 5.0) × log10(1)` = **0.0**     |
| 7.2          | 60         | `(7.2 - 5.0) × log10(11)` = **2.3**    |
| 7.2          | 1000       | `(7.2 - 5.0) × log10(951)` = **6.6**   |
| 8.5          | 10000      | `(8.5 - 5.0) × log10(9951)` = **13.9** |

---

### FavoriteGenreRating - genre_score
Each movie/series has a list of genres from TMDB. For each genre, the user's `FavoriteGenre.rating`
is mapped to a point value and averaged. Genres with 0 pts are excluded from the average to avoid
penalizing movies with many neutral genres. If all genres have 0 pts, `genre_score = 0`.

Additionally, a logarithmic multiplier is applied based on the number of positively matched genres
(rating >= 6.0) to reward broader preference alignment.
```
base_genre_score = (sum of points of genres (≠ 0) / count of genres (≠ 0)) × 3

positive_genres = count of genres where rating >= 6.0

multiplier = 1 + 0.25 × log2(positive_genres + 1)

genre_score = base_genre_score × multiplier
```

Interval: **-12 to ~20 pts**

**Rating to points mapping:**

| FavoriteGenre.rating | Points |
|----------------------|--------|
| 1.0 - 2.0            | -4 pts |
| 2.0 - 3.0            | -3 pts |
| 3.0 - 4.0            | -2 pts |
| 4.0 - 5.0            | -1 pts |
| 5.0 (default)        |  0 pts |
| 5.0 - 6.0            | +1 pts |
| 6.0 - 7.0            | +2 pts |
| 7.0 - 8.0            | +3 pts |
| 8.0 - 9.0            | +4 pts |
| 9.0 - 10.0           | +5 pts |

**Example:**
Movie with genres: `Action (rating 8.5)`, `Comedy (rating 6.2)`, `Thriller (no rating = 5.0)`, `Drama (no rating = 5.0)`
```
base_genre_score = ((4 + 2) / 2) × 3 = 9

positive_genres = 2

multiplier = 1 + 0.25 × log2(3) ≈ 1.4

genre_score = 9 × 1.4 = 12.6 pts
```
 
---

### release_bonus
Rewards newer titles. Films released within the last 3 months get a reduced bonus if they have
fewer than 50 votes — enough to appear in recommendations but not enough to dominate.

```
release_date < 3 months AND vote_count < 50  → release_bonus = +5 pts
release_date < 3 months AND vote_count >= 50 → release_bonus = +10 pts
release_date >= 3 months → release_bonus from table below
```

| Release date      | Bonus                              |
|-------------------|------------------------------------|
| Last 3 months     | +10 pts (or +5 if vote_count < 50) |
| 3 months - 1 year | +5 pts                             |
| 1 - 2 years ago   | +3 pts                             |
| 2 - 5 years ago   | +1 pt                              |
| 5 - 8 years ago   | 0 pts                              |
| 8 - 15 years ago  | -3 pts                             |
| 15 - 25 years ago | -5 pts                             |
| 25+ years ago     | -10 pts                            |

Interval: **-10 to +10 pts**

> Note: classics penalty (-3 to -10 pts) may be replaced with a user preference toggle in the future.

### popularity_score
Uses TMDB's own `popularity` metric which takes into account page views, user list additions, and rating trends.
Raw value has no upper bound so it's normalized with a logarithm to prevent it from dominating the final score.

```
popularity_score = log10(popularity + 1) × 2
```

**Examples:**

| popularity | popularity_score             |
|------------|------------------------------|
| 5          | `log10(6) × 2` = **1.56**    |
| 50         | `log10(51) × 2` = **3.42**   |
| 500        | `log10(501) × 2` = **5.40**  |
| 5000       | `log10(5001) × 2` = **7.40** |

Interval: **0 to ~8 pts**

## Tunable Parameters

| Parameter              | Value | Description                        |
|------------------------|-------|------------------------------------|
| `VOTE_COUNT_THRESHOLD` | 50    | Minimum vote count                 |
| `VOTE_AVERAGE_NEUTRAL` | 5.0   | Neutral point for rating           |
| `TOP_GENRES`           | 5     | Number of genres to query TMDB     |
| `TMDB_PAGES_PER_GENRE` | 3     | Number of TMDB pages per genre     |
| `RESULTS_LIMIT`        | 20    | Number of returned recommendations |

---

## Example

### User genre preferences
| Genre ID | Genre      | FavoriteGenre.rating | Points |
|----------|------------|----------------------|--------|
| 28       | Action     | 7.5                  | +3 pts |
| 27       | Horror     | 4.0                  | -1 pts |
| 80       | Crime      | 10.0                 | +5 pts |
| 10751    | Family     | 8.5                  | +4 pts |
| other    | (default)  | 5.0                  |  0 pts |

### Score breakdown

| Film                          | quality_score           | genre_score | release_bonus    | popularity_score | **final_score** |
|-------------------------------|-------------------------|-------------|------------------|------------------|-----------------|
| Puss in Boots: The Last Wish  | 12.28 (va:8.3, vc:5326) | 12.0        | +1 (2022-12-07)  | 6.30             | **31.58**       |
| Now You See Me: Now You Don't | 4.77 (va:6.5, vc:1441)  | 15.0        | +5 (2025-11-12)  | 3.33             | **28.10**       |
| Kill Boksoon                  | 3.83 (va:6.8, vc:184)   | 12.0        | +3 (2023-02-17)  | 5.96             | **24.79**       |
| Shazam! Fury of the Gods      | 5.51 (va:6.8, vc:1203)  | 9.0         | +3 (2023-03-15)  | 7.27             | **24.78**       |
| They Will Kill You            | 4.03 (va:6.7, vc:324)   | 1.5         | +10 (2026-03-25) | 4.78             | **20.31**       |
| Hot Fuzz                      | 10.03 (va:7.6, vc:8279) | 12.0        | -5 (2007-02-14)  | 3.0              | **20.03**       |
| Evil Dead Rise                | 4.09 (va:6.9, vc:192)   | -6.0        | +3 (2023-04-12)  | 6.56             | **7.65**        |
| Charlie's Angels              | 3.11 (va:5.9, vc:4490)  | 9.0         | -10 (2000-11-02) | 3.0              | **5.11**        |
| The Communion Girl            | 1.01 (va:6.3, vc:55)    | -6.0        | +3 (2023-02-10)  | 6.15             | **4.16**        |

### Notes
- **Puss in Boots** wins thanks to high `quality_score` (8.3 avg, 5326 votes) and Family genre match
- **Now You See Me** scores high on `genre_score` (Crime = +5) despite average rating
- **Hot Fuzz** drops despite great rating and matching genres due to -5 pts classics penalty (2007)
- **Charlie's Angels** last due to weak rating and -10 pts for 25+ year old release (2000)
- **Horror films** (Evil Dead Rise, The Communion Girl) penalized by user's low Horror rating (4.0 = -1 pt)