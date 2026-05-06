package filmly.service.impl;

import filmly.dto.content.MovieDetailDto;
import filmly.dto.content.SeriesDetailDto;
import filmly.dto.genre.GenreDto;
import filmly.dto.watchlist.WatchListRequestDto;
import filmly.dto.watchlist.WatchListResponseDto;
import filmly.exception.EntityAlreadyExistsException;
import filmly.exception.EntityNotFoundException;
import filmly.mapper.WatchListMapper;
import filmly.model.Content;
import filmly.model.Genre;
import filmly.model.User;
import filmly.model.WatchList;
import filmly.repository.ContentRepository;
import filmly.repository.GenreRepository;
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
    private final ContentRepository contentRepository;
    private final MovieServiceImpl movieService;
    private final SeriesServiceImpl seriesService;
    private final GenreRepository genreRepository;

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

        if (watchListRepository.findByUser_IdAndContent_ExternalIdAndContent_Type(
                user.getId(), requestDto.contentId(), requestDto.contentType()).isPresent()) {
            throw new EntityAlreadyExistsException("Content is already in your watchlist");
        }

        Content content = getOrCreateContent(requestDto.contentId(), requestDto.contentType());

        WatchList watchList = new WatchList();
        watchList.setUser(user);
        watchList.setContent(content);
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
                .findByUser_IdAndContent_ExternalIdAndContent_Type(
                        user.getId(), requestDto.contentId(), requestDto.contentType())
                .orElseThrow(() -> new EntityNotFoundException(
                        "WatchList", requestDto.contentId()));
        watchList.setWatchedAt(LocalDateTime.now());
        log.debug("User {} marked {} with id: {} as watched",
                email, requestDto.contentType(), requestDto.contentId());
        return watchListMapper.toDto(watchListRepository.save(watchList));
    }

    @Override
    @Transactional
    public void deleteFromWatchList(String email, WatchListRequestDto requestDto) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User", email));
        Optional<WatchList> watchList = watchListRepository
                .findByUser_IdAndContent_ExternalIdAndContent_Type(
                        user.getId(), requestDto.contentId(), requestDto.contentType());
        if (watchList.isEmpty()) {
            log.debug("Content {} not found in watchlist for user {}",
                    requestDto.contentId(), email);
            return;
        }
        watchListRepository.deleteByUser_IdAndContentIdAndContentType(
                user.getId(), requestDto.contentId(), requestDto.contentType());
        log.debug("Content {} removed from watchlist for user {}",
                requestDto.contentId(), email);
    }

    private Content getOrCreateContent(Long externalId, Content.ContentType type) {
        return contentRepository.findByExternalIdAndType(externalId, type)
                .orElseGet(() -> {
                    Content content = new Content();
                    content.setExternalId(externalId);
                    content.setType(type);
                    Content savedContent = populateContentDetails(content);
                    return contentRepository.save(savedContent);
                });
    }

    private Content populateContentDetails(Content content) {
        content.setCreatedAt(LocalDateTime.now());
        if (content.getType() == Content.ContentType.MOVIE) {
            MovieDetailDto movie = movieService.findById(content.getExternalId());
            content.setTitle(movie.title());
            content.setPosterPath(movie.posterPath());
            content.setReleaseDate(movie.releaseDate());
            content.setVoteAverage(movie.voteAverage());
            content.setVoteCount(movie.voteCount());
            content.setGenres(resolveGenres(
                    movie.genres().stream().map(GenreDto::name).toList()));
        } else {
            SeriesDetailDto series = seriesService.findById(content.getExternalId());
            content.setTitle(series.title());
            content.setPosterPath(series.posterPath());
            content.setReleaseDate(series.releaseDate());
            content.setVoteAverage(series.voteAverage());
            content.setVoteCount(series.voteCount());
            content.setGenres(resolveGenres(
                    series.genres().stream().map(GenreDto::name).toList()));
        }
        return content;
    }

    private List<Genre> resolveGenres(List<String> names) {
        return genreRepository.findAllByNameIn(names);
    }
}
