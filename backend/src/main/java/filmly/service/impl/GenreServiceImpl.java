package filmly.service.impl;

import filmly.model.Genre;
import filmly.service.GenreService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    // TODO implement logic, change return to DTO, do Exceptions
    @Override
    public Genre getGenreById(Long id) {
        return null;
    }

    @Override
    public List<Genre> getAllGenres() {
        return List.of();
    }

    @Override
    public Genre createGenre(Genre genre) {
        return null;
    }

    @Override
    public void deleteGenre(Long id) {

    }
}
