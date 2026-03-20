package filmly.dto.favoritegenres;

public record FavoriteGenreDto(
        Long favoriteGenreId,
        Long userId,
        Long genreId,
        Integer rating
) {}
