package filmly.service;

import filmly.dto.genre.GenreDto;
import java.util.List;

public interface GenreService {

    GenreDto getGenreById(Long id);

    List<GenreDto> getAllGenres();

    void syncGenres();
}
