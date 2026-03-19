package filmly.service;

import filmly.model.WatchList;
import java.util.List;

public interface WatchlistService {

    WatchList getWatchlistById(Long id);

    List<WatchList> getAllByUserId(Long userId);

    WatchList addToWatchlist(WatchList watchlist);

    void removeFromWatchlist(Long id);
}
