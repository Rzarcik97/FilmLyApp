# 🎬 FilmLy – Movie & Series Backend Application

FilmLy is a Spring Boot backend application for browsing and managing movies and series.
It integrates with the **TMDB API** to provide up-to-date content and offers personalized recommendations based on user genre preferences.

Key features include:
- Movie & series browsing with likes, ratings and watchlist
- Personalized recommendations scored by genre preferences, vote average, release date and popularity
- JWT-based authentication with email/password change verification flow
- Full CI pipeline with GitHub Actions and deployment on Railway

---

# 📚 Table of Contents

1. [Technologies](#technologies)
2. [Architecture](#architecture)
3. [Features & API Overview](#features)
4. [How to Run](#how-to-run)
5. [Database & Liquibase](#database)
6. [Testing](#testing)
7. [CI/CD](#cicd)

---

# <h1 id="technologies">🛠 Technologies</h1>

| Technology          | Description                             |
|---------------------|-----------------------------------------|
| Java 21             | Core language                           |
| Spring Boot 3.x     | Main framework                          |
| Spring Security     | Authentication & authorization          |
| Spring Data JPA     | Data persistence                        |
| JWT (jjwt)          | Token-based authentication              |
| MapStruct           | DTO mapping                             |
| Liquibase           | DB schema & seed data management        |
| Hibernate Validator | Input validation                        |
| Caffeine Cache      | In-memory caching for TMDB responses    |
| Testcontainers      | Integration tests with MySQL in Docker  |
| MySQL               | Production database                     |
| TMDB API            | External movie & series data source     |
| Swagger (springdoc) | API documentation                       |
| Lombok              | Boilerplate reduction                   |
| Log4j2              | Logging                                 |

---

# <h1 id="architecture">🏗️ Architecture</h1>

FilmLy is built as a layered Spring Boot backend. Most endpoints are publicly accessible, while user-specific features (watchlist, likes, ratings, recommendations) require JWT authentication.

```
                    ┌──────────────────────────────┐
                    │          Client Side         │
                    │  (Frontend / Postman / API)  │
                    └──────────────┬───────────────┘
                                   │ HTTP Requests
                                   ▼
                    ┌──────────────────────────────┐
                    │      Authentication Layer    │
                    │    Spring Security + JWT     │
                    │                              │
                    │  Public: /movies, /series,   │
                    │  /genres, /search, /actors   │
                    │                              │
                    │  Protected: /users,          │
                    │  /likes, /watchlist, /rating │
                    └──────────────┬───────────────┘
                                   │
                                   ▼
             ┌─────────────────────────────────────────────┐
             │               REST Controllers              │
             └───────────────────────┬─────────────────────┘
                                     │
                                     ▼
                   ┌─────────────────────────────────┐
                   │           Service Layer         │
                   │  Business logic, TMDB calls,    │
                   │  recommendation scoring,        │
                   │  Caffeine cache                 │
                   └──────────────┬──────────────────┘
                                  │
                                  ▼
                   ┌─────────────────────────────────┐
                   │        Persistence Layer        │
                   │   JPA Repositories + Entities   │
                   └──────────────┬──────────────────┘
                                  │
                                  ▼
                   ┌─────────────────────────────────┐
                   │            Database             │
                   │  Users / Content / WatchList    │
                   │  Likes / Ratings / Genres       │
                   └─────────────────────────────────┘
```

---

# <h1 id="features">📘 Features & API Overview</h1>

## 🌐 Public Endpoints (no authentication required)

### 🎬 Movies

| Action                        | Method | Endpoint                  |
|-------------------------------|--------|---------------------------|
| Get popular movies            | GET    | `/movies/popular`         |
| Get trending movies           | GET    | `/movies/trending`        |
| Get recent movies             | GET    | `/movies/recent`          |
| Get upcoming movies           | GET    | `/movies/upcoming`        |
| Get movie by ID               | GET    | `/movies/{id}`            |
| Get movie cast                | GET    | `/movies/{id}/cast`       |
| Get similar movies            | GET    | `/movies/{id}/similar`    |
| Get recommendations           | GET    | `/movies/recommendations` |

### 📺 Series

| Action                        | Method | Endpoint                   |
|-------------------------------|--------|----------------------------|
| Get popular series            | GET    | `/series/popular`          |
| Get trending series           | GET    | `/series/trending`         |
| Get recent series             | GET    | `/series/recent`           |
| Get series by ID              | GET    | `/series/{id}`             |
| Get series cast               | GET    | `/series/{id}/cast`        |
| Get similar series            | GET    | `/series/{id}/similar`     |
| Get recommendations           | GET    | `/series/recommendations`  |

### 🎭 Genres

| Action             | Method | Endpoint       |
|--------------------|--------|----------------|
| Get all genres     | GET    | `/genres`      |
| Get genre by ID    | GET    | `/genres/{id}` |

### 🔍 Search

| Action          | Method | Endpoint           |
|-----------------|--------|--------------------|
| Search by title | GET    | `/search`          |
| Discover        | GET    | `/search/discover` |

### 🎭 Actors

| Action              | Method | Endpoint          |
|---------------------|--------|-------------------|
| Get popular actors  | GET    | `/actors/popular` |

---

## 🔐 Protected Endpoints (JWT required)

### 👤 User

| Action                       | Method | Endpoint                        |
|------------------------------|--------|---------------------------------|
| Register                     | POST   | `/auth/registration`            |
| Login                        | POST   | `/auth/login`                   |
| Get my profile               | GET    | `/users/me`                     |
| Update my profile            | PATCH  | `/users/me`                     |
| Change email                 | PATCH  | `/users/me/change-email`        |
| Change password              | PATCH  | `/users/me/change-password`     |
| Verify email/password change | POST   | `/users/verify`                 |

### ❤️ Likes

| Action                   | Method | Endpoint |
|--------------------------|--------|----------|
| Toggle like / dislike    | POST   | `/likes` |
| Get liked/disliked content | GET  | `/likes` |

### 📋 Watchlist

| Action                  | Method | Endpoint                      |
|-------------------------|--------|-------------------------------|
| Get watchlist           | GET    | `/users/watchlist`            |
| Add to watchlist        | POST   | `/users/watchlist`            |
| Mark as watched         | PATCH  | `/users/watchlist/watched`    |
| Remove from watchlist   | DELETE | `/users/watchlist/{contentId}`|

### ⭐ Ratings

| Action          | Method | Endpoint           |
|-----------------|--------|--------------------|
| Get ratings     | GET    | `/user/rating`     |
| Add rating      | POST   | `/user/rating`     |
| Update rating   | PATCH  | `/user/rating`     |
| Delete rating   | DELETE | `/user/rating/{id}`|

### 🎭 Favorite Genres

| Action                      | Method | Endpoint                          |
|-----------------------------|--------|-----------------------------------|
| Get all favorite genres     | GET    | `/users/favorite-genres`          |
| Get sorted favorite genres  | GET    | `/users/favorite-genres/sorted`   |
| Add favorite genre          | POST   | `/users/favorite-genres`          |
| Update favorite genre       | PATCH  | `/users/favorite-genres`          |
| Delete favorite genre       | DELETE | `/users/favorite-genres`          |

---

# <h1 id="how-to-run">🚀 How to Run</h1>

## ✅ Prerequisites

- Java 21+
- Maven
- MySQL (or Docker)
- TMDB API account — get free tokens at [themoviedb.org](https://www.themoviedb.org/)

## 🔐 Environment Variables

Create a `.env` file in the project root:

```bash
cp .envex .env
```

Fill in the values:

```env
# DATABASE CONFIG
MYSQLDB_DATABASE=filmly
DB_URL=jdbc:mysql://localhost:3306/filmly?serverTimeZone=UTC
MYSQLDB_USER=root
MYSQLDB_ROOT_PASSWORD=your_password
DB_DRIVER=com.mysql.cj.jdbc.Driver

# PORTS
MYSQLDB_LOCAL_PORT=3307
MYSQLDB_DOCKER_PORT=3306
SPRING_LOCAL_PORT=8081
SPRING_DOCKER_PORT=8080

# JWT CONFIG
JWT_SECRET=your-very-long-secret-key
JWT_EXPIRATION=300

# TMDB CONFIG
TMDB_API_TOKEN=your_tmdb_api_token
TMDB_READ_TOKEN=your_tmdb_read_token

# API DOCS
API_DOC_ENABLE=true
```

## ▶️ Run locally

```bash
mvn spring-boot:run
```

Application starts at `http://localhost:8081`

Swagger UI available at `http://localhost:8081/swagger-ui/index.html`

# <h1 id="database">🗄 Database & Liquibase</h1>

The project uses **Liquibase** for database schema management. On startup Liquibase automatically:
- creates all tables with proper relationships and constraints
- seeds genres from TMDB on first startup via `DataInitializer`

Schema files are located in:
```
src/main/resources/db/changelog/
```

### Production changelog
```
001-init-schema.yml   — creates all tables
```

Genres are not seeded via Liquibase on production — they are fetched from TMDB API on first application startup via `syncGenres()`. If genres already exist in the database, sync is skipped.

---

# <h1 id="testing">🧪 Testing</h1>

The project includes comprehensive test coverage across all layers.

### ✔ Unit Tests (Mockito)
- All service implementations: MovieService, SeriesService, ContentService, GenreService, ActorService, ContentLikeService, WatchListService, UserService, RecommendationScorer
- Error handling and business logic

### ✔ Integration Tests (Testcontainers + MySQL)
- Repository custom queries: ContentLikeRepository, GenreRepository, ContentRatingRepository, ContentRepository, WatchListRepository
- Real MySQL database via Testcontainers — matches production environment

### ✔ Controller Tests (MockMvc + SpringBootTest)
- All endpoints with authenticated and anonymous scenarios
- Validation rules and HTTP status codes
- Security checks (401 for protected endpoints)

### Test database
Integration tests use a separate Liquibase changelog with seed data:
```
src/test/resources/db/changelog/
  001-init-schema.yml     — creates tables
  002-seed-genres.yml     — inserts all genres
  003-test-data.yml       — inserts test users and content
```

Run all tests:
```bash
mvn test
```

> ⚠️ Integration tests require **Docker** installed locally (used by Testcontainers to spin up MySQL).

---

# <h1 id="cicd">🚀 CI/CD</h1>

The project uses **GitHub Actions** for continuous integration. On every push and pull request the pipeline automatically:
- checks out the repository
- installs JDK 21
- runs the full test suite including integration tests
- builds the JAR artifact

### `.github/workflows/ci.yml`

```yaml
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout repo
        uses: actions/checkout@v3

      - name: Set up Java
        uses: actions/setup-java@v3
        with:
          distribution: temurin
          java-version: 21
          cache: maven

      - name: Build backend
        working-directory: backend
        env:
          JWT_SECRET: ${{ secrets.JWT_SECRET }}
          JWT_EXPIRATION: ${{ secrets.JWT_EXPIRATION }}
          TMDB_READ_TOKEN: ${{ secrets.TMDB_READ_TOKEN }}
          TMDB_API_TOKEN: ${{ secrets.TMDB_API_TOKEN }}
        run: mvn --batch-mode clean verify
```