package filmly.controller;

import filmly.dto.watchlist.WatchListRequestDto;
import filmly.dto.watchlist.WatchListResponseDto;
import filmly.model.Content;
import filmly.service.WatchListService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/watchlist")
@RequiredArgsConstructor
public class WatchListController {

    private final WatchListService watchListService;

    @GetMapping
    @Operation(summary = "Get Watchlist",
            description = "Retrieve watchlist of the authenticated user. "
                    + "Pass watched=false to get only unwatched content, "
                    + "watched=true or no param returns all")
    public List<WatchListResponseDto> getWatchList(
            @RequestParam(defaultValue = "true") Boolean showWatched,
            Authentication authentication) {
        String email = authentication.getName();
        return watchListService.getWatchList(email, showWatched);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add to Watchlist",
            description = "Add a movie or series to the authenticated user's watchlist")
    public WatchListResponseDto addToWatchList(@RequestBody @Valid WatchListRequestDto dto,
                                               Authentication authentication) {
        log.info("Adding content {} to watchlist for user {}",
                dto.contentId(), authentication.getName());
        String email = authentication.getName();
        return watchListService.addToWatchList(email, dto);
    }

    @PatchMapping("/{contentId}/watched")
    @Operation(summary = "Mark as Watched",
            description = "Mark a movie or series as watched in the authenticated user's watchlist")
    public WatchListResponseDto markAsWatched(@RequestBody @Valid WatchListRequestDto requestDto,
                                              Authentication authentication) {
        String email = authentication.getName();
        log.info("Marking content {} as watched for user {}", requestDto.contentId(), email);
        return watchListService.markAsWatched(email, requestDto);
    }

    @DeleteMapping("/{contentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove from Watchlist",
            description = "Remove a movie or series from the authenticated user's watchlist")
    public void deleteFromWatchList(@PathVariable Long contentId,
                                    @RequestParam Content.ContentType contentType,
                                    Authentication authentication) {
        String email = authentication.getName();
        log.info("Removing content {} from watchlist for user {}", contentId, email);
        watchListService.deleteFromWatchList(email, contentId, contentType);
    }
}
