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
@RequestMapping("/user/rating")
public class RatingController {

    @Deprecated
    @GetMapping("/{contentType}")
    public ResponseEntity<?> getRatings(@PathVariable String contentType) {
        return ResponseEntity.ok().build();
    }

    @Deprecated
    @PostMapping("/{contentType}/{id}")
    public ResponseEntity<?> addRating(@PathVariable String contentType,
                                       @PathVariable Long id,
                                       @RequestBody Object ratingRequest) {
        return ResponseEntity.status(201).build();
    }

    @Deprecated
    @DeleteMapping("/{contentType}/{id}")
    public ResponseEntity<?> removeRating(@PathVariable String contentType,
                                          @PathVariable Long id) {
        return ResponseEntity.noContent().build();
    }

    @Deprecated
    @PatchMapping("/{contentType}/{id}")
    public ResponseEntity<?> updateRating(@PathVariable String contentType,
                                          @PathVariable Long id,
                                          @RequestBody Object ratingRequest) {
        return ResponseEntity.ok().build();
    }
}
