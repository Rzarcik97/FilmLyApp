package filmly.controller;

import filmly.dto.favoritegenres.FavoriteGenreDto;
import filmly.dto.genre.FavoriteGenreResponseDto;
import filmly.service.FavoriteGenreService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/users/favorite-genres")
@RequiredArgsConstructor
public class FavoriteGenreController {

    private final FavoriteGenreService favoriteGenreService;

    @GetMapping("/sorted")
    @Operation(summary = "Get Sorted Favorite Genres",
            description = "Retrieve all genres sorted by user's rating (descending). "
                    + "Genres not yet rated by the user are included with a neutral rating of 5.0")
    public List<FavoriteGenreResponseDto> getSortedFavoriteGenres(Authentication authentication) {
        String email = authentication.getName();
        return favoriteGenreService.getSortedFavoriteGenreByUserId(email);
    }

    @GetMapping
    @Operation(summary = "Get All Favorite Genres",
            description = "Retrieve all explicitly rated favorite genres for a given user")
    public List<FavoriteGenreResponseDto> getAllFavoriteGenres(Authentication authentication) {
        String email = authentication.getName();
        return favoriteGenreService.getAllByUserId(email);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create Favorite Genre",
            description = "Add a new favorite genre with a rating for the specified user. "
                    + "If the genre is already rated, the existing rating will be updated instead")
    public FavoriteGenreResponseDto createFavoriteGenre(
            Authentication authentication,
            @RequestBody @Valid FavoriteGenreDto favoriteGenreDto) {
        String email = authentication.getName();
        log.info("Creating favorite genre '{}' for user {}",
                favoriteGenreDto.genreName(), email);
        return favoriteGenreService.createFavoriteGenre(email, favoriteGenreDto);
    }

    @PatchMapping
    @Operation(summary = "Update Favorite Genre",
            description = "Update the rating of an already existing favorite "
                    + "genre for the specified user")
    public FavoriteGenreResponseDto updateFavoriteGenre(
            Authentication authentication,
            @RequestBody @Valid FavoriteGenreDto favoriteGenreDto) {
        String email = authentication.getName();
        log.info("Updating favorite genre '{}' for user {}",
                favoriteGenreDto.genreName(), email);
        return favoriteGenreService.updateFavoriteGenre(email, favoriteGenreDto);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete Favorite Genre",
            description = "Remove a favorite genre rating for the specified user by genre name")
    public void deleteFavoriteGenre(Authentication authentication,
                                    @RequestParam String genreName) {
        String email = authentication.getName();
        log.info("Deleting favorite genre '{}' for user {}", genreName, email);
        favoriteGenreService.deleteFavoriteGenre(email, genreName);
    }
}
