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
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
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
    public WatchListResponseDto addToWatchList(String email, WatchListRequestDto requestDto) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User", email));
        if (watchListRepository.findByUser_IdAndContentIdAndContentType(
                user.getId(), requestDto.contentId(), requestDto.contentType()).isPresent()) {
            throw new EntityAlreadyExistsException("Content is already in your watchlist");
        }
        String title;
        String posterPath;

        if (requestDto.contentType() == Content.ContentType.MOVIE) {
            MovieDetailDto movie = movieService.findById(requestDto.contentId());
            title = movie.title();
            posterPath = movie.posterPath();
        } else {
            SeriesDetailDto series = seriesService.findById(requestDto.contentId());
            title = series.title();
            posterPath = series.posterPath();
        }

        WatchList watchList = new WatchList();
        watchList.setUser(user);
        watchList.setContentId(requestDto.contentId());
        watchList.setContentType(requestDto.contentType());
        watchList.setTitle(title);
        watchList.setPosterPath(posterPath);
        watchList.setAddedAt(LocalDateTime.now());
        log.info("User {} added {} with id: {} to watchlist",
                email, requestDto.contentType(), requestDto.contentId());
        return watchListMapper.toDto(watchListRepository.save(watchList));
    }

    @Override
    public WatchListResponseDto markAsWatched(String email, WatchListRequestDto requestDto) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User", email));
        WatchList watchList = watchListRepository
                .findByUser_IdAndContentIdAndContentType(
                        user.getId(), requestDto.contentId(), requestDto.contentType())
                .orElseThrow(() -> new EntityNotFoundException(
                        "WatchList", requestDto.contentId()));
        watchList.setWatchedAt(LocalDateTime.now());
        log.info("User {} marked {} with id: {} as watched",
                email, requestDto.contentType(), requestDto.contentId());
        return watchListMapper.toDto(watchListRepository.save(watchList));
    }

    @Override
    @Transactional
    public void deleteFromWatchList(String email, WatchListRequestDto requestDto) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User", email));
        Optional<WatchList> watchList = watchListRepository
                .findByUser_IdAndContentIdAndContentType(
                        user.getId(), requestDto.contentId(), requestDto.contentType());
        if (watchList.isEmpty()) {
            log.warn("Content {} not found in watchlist for user {}",
                    requestDto.contentId(), email);
            return;
        }
        watchListRepository.deleteByUser_IdAndContentIdAndContentType(
                user.getId(), requestDto.contentId(), requestDto.contentType());
        log.info("Content {} removed from watchlist for user {}",
                requestDto.contentId(), email);
    }
}
