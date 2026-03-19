package filmly.service.impl;

import filmly.model.WatchList;
import filmly.service.WatchlistService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WatchlistServiceImpl implements WatchlistService {
    // TODO implement logic, change return to DTO, do Exceptions
    @Override
    public WatchList getWatchlistById(Long id) {
        return null;
    }

    @Override
    public List<WatchList> getAllByUserId(Long userId) {
        return List.of();
    }

    @Override
    public WatchList addToWatchlist(WatchList watchlist) {
        return null;
    }

    @Override
    public void removeFromWatchlist(Long id) {

    }
}
