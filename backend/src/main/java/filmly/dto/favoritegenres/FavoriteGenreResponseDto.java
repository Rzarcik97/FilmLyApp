package filmly.dto.favoritegenres;

import filmly.enums.GenreType;

public record FavoriteGenreResponseDto(
        Long id,
        String genreName,
        String genreImagePath,
        GenreType genreType,
        Float rating
) {}
