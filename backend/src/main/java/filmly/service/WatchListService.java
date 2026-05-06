package filmly.service;

import filmly.dto.content.ContentDto;
import filmly.dto.watchlist.WatchListRequestDto;
import filmly.dto.watchlist.WatchListResponseDto;
import filmly.model.Content;
import java.util.List;

public interface WatchListService {

    List<ContentDto> getWatchList(String email,
                                  Boolean watched,
                                  Content.ContentType type);

    WatchListResponseDto addToWatchList(String email, WatchListRequestDto requestDto);

    WatchListResponseDto markAsWatched(String email, WatchListRequestDto requestDto);

    void deleteFromWatchList(String email, WatchListRequestDto requestDto);
}
