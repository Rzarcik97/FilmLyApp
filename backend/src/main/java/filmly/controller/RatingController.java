package filmly.controller;

import filmly.dto.contentrating.ContentRatingRequestDto;
import filmly.dto.contentrating.ContentRatingResponseDto;
import filmly.service.ContentRatingService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/user/rating")
@RequiredArgsConstructor
public class RatingController {

    private final ContentRatingService contentRatingService;

    @PostMapping
    @Operation(summary = "Add Rating",
            description = "Add a rating for a movie or series for the authenticated user")
    public ContentRatingResponseDto addRating(@RequestBody @Valid ContentRatingRequestDto dto,
                                              Authentication authentication) {
        String email = authentication.getName();
        return contentRatingService.addRating(email, dto);
    }

    @PatchMapping
    @Operation(summary = "Update Rating",
            description = "Update an existing rating for a "
                    + "movie or series for the authenticated user")
    public ContentRatingResponseDto updateRating(@RequestBody @Valid ContentRatingRequestDto dto,
                                             Authentication authentication) {
        String email = authentication.getName();
        return contentRatingService.updateRating(email, dto);
    }

    @DeleteMapping
    @Operation(summary = "Delete Rating",
            description = "Remove a rating for a movie or series for the authenticated user")
    public ResponseEntity<Void> deleteRating(@RequestBody @Valid ContentRatingRequestDto dto,
                                             Authentication authentication) {
        String email = authentication.getName();
        contentRatingService.deleteRating(email, dto);
        return ResponseEntity.noContent().build();
    }
}
