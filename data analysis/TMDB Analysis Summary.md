# TMDB Movie Dataset Analysis

## Project Overview
The main purpose of the analysis was to explore which movie attributes are associated with:

- **audience engagement** — using `vote_count` as a proxy
- **audience evaluation** — using `vote_average` as a proxy
- **commercial performance** — using `revenue` as a proxy

The dataset contains metadata for 10,000 popular movies from TMDB, including financial, descriptive, and audience-related fields.

---

## Analysis Goals
The analysis was designed to answer the following questions:

1. Which movie attributes are related to **audience engagement** (`vote_count`)?
2. Which attributes are related to **audience evaluation** (`vote_average`)?
3. Which attributes are associated with **revenue**?
4. Do genre, runtime, language, and release period influence engagement, ratings, and revenue?
5. Can movie metadata provide useful signals for a future recommendation-oriented product?

---

## Key Limitations
- The dataset contains **popular TMDB movies**, not a random sample of all films.
- `popularity` is a TMDB-specific metric, so it is less transparent than `vote_count`, `vote_average`, or `revenue`.
- Financial fields are incomplete and may not reflect full lifetime performance.
- Genre categories overlap.
- Country values may include co-productions, which can complicate country-level comparisons.
- Some language and country groups have very small sample sizes.
- Mean financial metrics can be strongly distorted by outliers, so medians are often more reliable.

---

## [Overview in Tableau](https://public.tableau.com/app/profile/kseniia.kornienko/viz/TMDBMovies_17761972864920/OverviewofTMDBMovieDS?publish=yes)
## What Was Done

### 1. Data loading and preparation
- Loaded the original JSON-based TMDB movie dataset into pandas.
- Selected the main fields relevant for analysis, including:
  - `budget`
  - `revenue`
  - `popularity`
  - `runtime`
  - `vote_average`
  - `vote_count`
  - `release_date`
  - `genres`
  - `original_language`
  - `origin_country`
  - `tagline`
  - `production_company_name`

### 2. Data cleaning
- Converted `release_date` to datetime format.
- Removed rows with missing critical fields such as `release_date` and `genres` where necessary.
- Replaced empty text values in fields such as `tagline` with explicit placeholders.
- Treated `budget = 0` and `revenue = 0` as missing values.
- Treated very small financial values (for example, under 10,000) as missing values because they are likely unreliable or non-informative for financial analysis.
- Checked duplicates and dataset consistency.
- Removed columns with no analytical value, such as `adult`, when they contained only one category.

### 3. Feature understanding
- Reviewed the structure of the dataset and the meaning of the available variables.
- Assessed the completeness of the main financial variables.
- Identified that the dataset is heavily skewed toward English-language and US-linked productions.
- Noted that one movie can belong to multiple genres, so genre counts are not mutually exclusive.

### 4. Exploratory analysis
- Parsed and exploded the `genres` field for genre-level analysis.
- Built summary tables for:
  - genres
  - languages
  - countries
  - production companies
- Explored relationships among the key metrics:
  - `vote_count`
  - `vote_average`
  - `revenue`
  - `budget`
  - `runtime`
  - `roi`
- Used **Spearman correlation** to evaluate relationships between skewed numeric variables.
- Created a correlation heatmap to interpret the strength of relationships among the key metrics.

### 5. Hypothesis testing and exploratory ideas
The analysis included several initial hypotheses, such as:
- movies with higher audience engagement (`vote_count`) may also generate higher revenue
- higher-budget movies may generate higher revenue
- some genres may perform better than others in terms of engagement or revenue
- runtime may be related to rating or engagement
- shorter taglines might be associated with stronger audience response

Not all exploratory hypotheses were supported. For example, the idea that shorter taglines might lead to stronger engagement was not meaningfully confirmed by the analysis.

---

## Main Findings So Far

### Audience engagement and revenue
- `vote_count` shows a strong positive relationship with `revenue`.
- Movies with more votes tend to generate higher revenue.
- This suggests that audience attention and commercial performance are closely related in the dataset.

### Revenue and budget
- `budget` shows one of the strongest positive relationships with `revenue`.
- Larger-budget movies tend to generate higher reported revenue.

### Vote average behaves differently
- `vote_average` has a much weaker relationship with `revenue` than `vote_count`.
- This suggests that average rating is less connected to commercial success than audience reach or engagement.

### Runtime has limited explanatory power
- Runtime shows only a weak relationship with revenue.
- Its relationship with audience evaluation is slightly stronger, but still limited.

### Genre-level interpretation requires caution
- Since one movie can belong to multiple genres, genre-level counts and percentages overlap.
- Genre analysis is still useful, but categories are not mutually exclusive.

### Financial metrics should be interpreted carefully
- `revenue` and `budget` in TMDB should not be treated as complete profit-and-loss data.
- A ratio such as `revenue / budget` is better interpreted as a **reported revenue-to-budget ratio**, not true profitability.
- Therefore, cases where `revenue < budget` should not automatically be interpreted as actual financial loss.

### Recommendations: 
1: Use vote_count as a stronger signal of audience engagement than vote_average when exploring recommendation features.
2: Treat vote_average as a quality-related metric, but not as a strong indicator of commercial success.
3: Prioritize genre and genre combinations as key metadata features for recommendation logic.
4: Use revenue carefully as a proxy for commercial performance, while clearly acknowledging data completeness limitations.
5: Exclude tagline length from core recommendation features, as the analysis did not show a meaningful relationship with engagement.
6: Build dashboards and future analysis around engagement and evaluation as separate dimensions.
---

## Short Summary
This project explored a TMDB movie dataset in order to understand how movie metadata relates to audience engagement, ratings, and commercial performance. The work included data cleaning, exploratory analysis, genre parsing, summary tables, and correlation analysis. The strongest signals identified so far suggest that **vote_count** and **budget** are more strongly associated with **revenue** than `vote_average`, while some exploratory ideas such as tagline length did not show meaningful impact. The analysis provides a solid foundation for future Tableau dashboards and recommendation-oriented product thinking.
