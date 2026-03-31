package filmly.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Deprecated
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Object registerRequest) {
        // TODO: replace Object with RegisterRequest DTO
        // TODO: implement
        return ResponseEntity.status(201).build();
    }

    @Deprecated
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Object loginRequest) {
        // TODO: replace Object with LoginRequest DTO
        // TODO: implement – authenticate and return JWT / session token
        return ResponseEntity.ok().build();
    }

    @Deprecated
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // TODO: resolve current user from SecurityContext
        // TODO: implement – invalidate token / session
        return ResponseEntity.noContent().build();
    }
}
