package filmly.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/favorite-genres")
public class FavoriteGenresController {
    @GetMapping
    public ResponseEntity<?> getFavoriteGenres() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{genreName}")
    public ResponseEntity<?> addFavoriteGenre(@PathVariable String genreName) {
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/{genreName}")
    public ResponseEntity<?> removeFavoriteGenre(@PathVariable String genreName) {
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{genreName}")
    public ResponseEntity<?> updateFavoriteGenre(@PathVariable String genreName,
                                                 @RequestBody Object updateRequest) {
        return ResponseEntity.ok().build();
    }
}
