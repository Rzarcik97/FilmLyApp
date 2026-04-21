package filmly.service;

import filmly.dto.watchlist.WatchListRequestDto;
import filmly.dto.watchlist.WatchListResponseDto;
import java.util.List;

public interface WatchListService {

    List<WatchListResponseDto> getWatchList(String email, Boolean watched);

    WatchListResponseDto addToWatchList(String email, WatchListRequestDto requestDto);

    WatchListResponseDto markAsWatched(String email, WatchListRequestDto requestDto);

    void deleteFromWatchList(String email, WatchListRequestDto requestDto);
}
