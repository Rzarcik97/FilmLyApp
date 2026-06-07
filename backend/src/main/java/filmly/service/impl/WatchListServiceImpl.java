package filmly.service.impl;

import filmly.dto.contentlikes.ContentLikeResponseDto;
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
import filmly.service.ContentLikeService;
import filmly.service.ContentService;
import filmly.service.WatchListService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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
    private final ContentLikeService contentLikeService;
    private final ContentService contentService;

    @Override
    public List<WatchListResponseDto> getWatchList(
            String email,
            Boolean showWatched,
            Content.ContentType type) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User", email));

        List<WatchList> results = showWatched.equals(false)
                ? watchListRepository.findByUser_IdAndWatchedAtIsNullAndContent_Type(
                        user.getId(), type)
                : watchListRepository.findByUser_IdAndContent_Type(user.getId(), type);

        List<Long> contentIds = results.stream()
                .map(w -> w.getContent().getExternalId())
                .toList();

        Map<Long, ContentLikeResponseDto> likesMap = contentLikeService
                .getLikesByContentIds(contentIds, type);

        return results.stream()
                .map(r -> {
                    ContentLikeResponseDto likes = likesMap.get(r.getContent().getExternalId());
                    return watchListMapper.toDtoWithLikes(r, likes.likes(),likes.dislikes());
                })
                .toList();
    }

    @Override
    public WatchListResponseDto addToWatchList(String email, WatchListRequestDto requestDto) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User", email));

        if (watchListRepository.findByUser_IdAndContent_ExternalIdAndContent_Type(
                user.getId(), requestDto.contentId(), requestDto.contentType()).isPresent()) {
            throw new EntityAlreadyExistsException("Content is already in your watchlist");
        }

        Content content = contentService.getOrCreate(
                requestDto.contentId(),
                requestDto.contentType());

        WatchList watchList = new WatchList();
        watchList.setUser(user);
        watchList.setContent(content);
        watchList.setAddedAt(LocalDateTime.now());

        ContentLikeResponseDto likes = contentLikeService.getLikes(
                requestDto.contentId(), requestDto.contentType());

        log.info("User {} added {} with id: {} to watchlist",
                email, requestDto.contentType(), requestDto.contentId());
        return watchListMapper.toDtoWithLikes(
                watchListRepository.save(watchList), likes.likes(), likes.dislikes());
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

        ContentLikeResponseDto likes = contentLikeService.getLikes(
                requestDto.contentId(), requestDto.contentType());

        log.debug("User {} marked {} with id: {} as watched",
                email, requestDto.contentType(), requestDto.contentId());
        return watchListMapper.toDtoWithLikes(
                watchListRepository.save(watchList), likes.likes(), likes.dislikes());
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
        watchListRepository.delete(watchList.get());
        log.debug("Content {} removed from watchlist for user {}",
                requestDto.contentId(), email);
    }
}
