package filmly.controller;

import filmly.dto.genre.GenreDto;
import filmly.exception.ErrorResponse;
import filmly.service.GenreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Get all genres")
    public List<GenreDto> getAllGenres() {
        return genreService.getAllGenres();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get genre by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Genre not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public GenreDto getGenreById(@PathVariable Long id) {
        return genreService.getGenreById(id);
    }
}
