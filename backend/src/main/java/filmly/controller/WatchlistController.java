package filmly.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/watchlist")
public class WatchlistController {
    @GetMapping("/{contentType}")
    public ResponseEntity<?> getWatchlist(@PathVariable String contentType) {
        // TODO: resolve current user from SecurityContext
        // TODO: implement
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{contentType}/{id}")
    public ResponseEntity<?> addToWatchlist(@PathVariable String contentType,
                                            @PathVariable Long id) {
        // TODO: resolve current user from SecurityContext
        // TODO: implement
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/{contentType}/{id}")
    public ResponseEntity<?> removeFromWatchlist(@PathVariable String contentType,
                                                 @PathVariable Long id) {
        // TODO: resolve current user from SecurityContext
        // TODO: implement
        return ResponseEntity.noContent().build();
    }

}
