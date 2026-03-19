package filmly.service;

import filmly.model.Genre;
import java.util.List;

public interface GenreService {

    Genre getGenreById(Long id);

    List<Genre> getAllGenres();

    Genre createGenre(Genre genre);

    void deleteGenre(Long id);
}
