package filmly.controller;

import filmly.dto.contentlikes.ContentLikeRequestDto;
import filmly.service.ContentLikeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class ContentLikeController {

    private final ContentLikeService contentLikeService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Toggle Like/Dislike",
            description = "Like or dislike a movie or series. "
                    + "Clicking the same reaction again removes it,"
                    + " clicking the opposite switches it")
    public void toggleLike(@RequestBody @Valid ContentLikeRequestDto dto,
                           Authentication authentication) {
        contentLikeService.toggleLike(authentication.getName(), dto);
    }
}
