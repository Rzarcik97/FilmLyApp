package filmly.controller;

import filmly.dto.genre.GenreDto;
import filmly.service.GenreService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173") // temporal thing to make frontend work with the backend
public class GenreController {

    private final GenreService genreService;

    @GetMapping
    public List<GenreDto> getAllGenres() {
        return genreService.getAllGenres();
    }

    @GetMapping("/{id}")
    public GenreDto getGenreById(@PathVariable Long id) {
        return genreService.getGenreById(id);
    }
}
