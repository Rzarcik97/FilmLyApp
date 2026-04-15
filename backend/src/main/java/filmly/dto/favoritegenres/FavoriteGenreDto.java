package filmly.dto.favoritegenres;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record FavoriteGenreDto(
        @NotBlank String genreName,
        @Min(1) @Max(10) Float rating
) {}
