package filmly.controller;

import filmly.dto.content.ActorDto;
import filmly.service.ActorService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/actors")
@RequiredArgsConstructor
public class ActorController {
    private final ActorService actorService;

    @Operation(summary = "Get popular actors")
    @GetMapping("/popular")
    public List<ActorDto> getPopularActors() {
        return actorService.findPopular();
    }
}
