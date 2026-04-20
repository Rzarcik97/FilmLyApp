package filmly.service.impl;

import filmly.dto.content.MovieDetailDto;
import filmly.dto.content.SeriesDetailDto;
import filmly.dto.watchlist.WatchListRequestDto;
import filmly.dto.watchlist.WatchListResponseDto;
import filmly.exception.EntityAlreadyExistsException;
import filmly.exception.EntityNotFoundException;
import filmly.mapper.WatchListMapper;
import filmly.model.Content;
import filmly.model.User;
import filmly.model.WatchList;
import filmly.repository.UserRepository;
import filmly.repository.WatchListRepository;
import filmly.service.WatchListService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WatchListServiceImpl implements WatchListService {

    private final WatchListRepository watchListRepository;
    private final UserRepository userRepository;
    private final WatchListMapper watchListMapper;
    private final MovieServiceImpl movieService;
    private final SeriesServiceImpl seriesService;

    @Override
    public List<WatchListResponseDto> getWatchList(String email, Boolean showWatched) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User", email));
        List<WatchList> result = showWatched.equals(false)
                ? watchListRepository.findByUser_IdAndWatchedAtIsNull(user.getId())
                : watchListRepository.findByUser_Id(user.getId());
        return watchListMapper.toDtoList(result);
    }

    @Override
    public WatchListResponseDto addToWatchList(String email, WatchListRequestDto requestedDto) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User", email));
        if (watchListRepository.findByUser_IdAndContentIdAndContentType(
                user.getId(), requestedDto.contentId(), requestedDto.contentType()).isPresent()) {
            throw new EntityAlreadyExistsException("Content is already in your watchlist");
        }
        String title;
        String posterPath;

        if (requestedDto.contentType() == Content.ContentType.MOVIE) {
            MovieDetailDto movie = movieService.findById(requestedDto.contentId());
            title = movie.title();
            posterPath = movie.posterPath();
        } else {
            SeriesDetailDto series = seriesService.findById(requestedDto.contentId());
            title = series.title();
            posterPath = series.posterPath();
        }

        WatchList watchList = new WatchList();
        watchList.setUser(user);
        watchList.setContentId(requestedDto.contentId());
        watchList.setContentType(requestedDto.contentType());
        watchList.setTitle(title);
        watchList.setPosterPath(posterPath);
        watchList.setAddedAt(LocalDateTime.now());
        return watchListMapper.toDto(watchListRepository.save(watchList));
    }

    @Override
    public WatchListResponseDto markAsWatched(String email, WatchListRequestDto dto) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User", email));
        WatchList watchList = watchListRepository
                .findByUser_IdAndContentIdAndContentType(
                        user.getId(), dto.contentId(), dto.contentType())
                .orElseThrow(() -> new EntityNotFoundException("WatchList", dto.contentId()));
        watchList.setWatchedAt(LocalDateTime.now());
        return watchListMapper.toDto(watchListRepository.save(watchList));
    }

    @Override
    @Transactional
    public void deleteFromWatchList(String email, Long contentId, Content.ContentType contentType) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User", email));
        watchListRepository.deleteByUser_IdAndContentIdAndContentType(
                user.getId(), contentId, contentType);
    }
}
