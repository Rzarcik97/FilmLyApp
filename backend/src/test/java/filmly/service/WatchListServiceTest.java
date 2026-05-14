package filmly.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import filmly.service.impl.WatchListServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class WatchListServiceTest {

    @Mock
    private WatchListRepository watchListRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WatchListMapper watchListMapper;

    @Mock
    private ContentLikeService contentLikeService;

    @Mock
    private ContentService contentService;

    @InjectMocks
    private WatchListServiceImpl watchListServiceImpl;

    // ── helpers ───────────────────────────────────────────────────────────────

    private User buildUser(Long id, String email) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        return user;
    }

    private Content buildContent(Long externalId, Content.ContentType type) {
        Content content = new Content();
        content.setExternalId(externalId);
        content.setType(type);
        content.setTitle("Test Movie");
        content.setPosterPath("/poster.jpg");
        content.setReleaseDate("2024-01-01");
        content.setVoteAverage(7.5);
        content.setVoteCount(100);
        content.setGenres(List.of());
        return content;
    }

    private WatchList buildWatchList(User user, Content content) {
        WatchList watchList = new WatchList();
        watchList.setId(1L);
        watchList.setUser(user);
        watchList.setContent(content);
        watchList.setAddedAt(LocalDateTime.now());
        return watchList;
    }

    private WatchListResponseDto buildResponseDto(Long id) {
        return new WatchListResponseDto(
                id, 1L, Content.ContentType.MOVIE, "Test Movie",
                "/poster.jpg", List.of(), "2024-01-01", 7.5, 100,
                0L, 0L, null, LocalDateTime.now()
        );
    }

    private ContentLikeResponseDto noLikes() {
        return new ContentLikeResponseDto(0L, 0L);
    }

    // ── getWatchList ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("""
            getWatchList | Returns unwatched items when showWatched is false
            """)
    void getWatchList_ShowWatchedFalse_ShouldReturnUnwatched() {
        // Given
        String email = "john@test.com";
        User user = buildUser(1L, email);
        Content content = buildContent(100L, Content.ContentType.MOVIE);
        WatchList watchList = buildWatchList(user, content);
        WatchListResponseDto dto = buildResponseDto(1L);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(watchListRepository.findByUser_IdAndWatchedAtIsNullAndContent_Type(
                user.getId(), Content.ContentType.MOVIE))
                .thenReturn(List.of(watchList));
        when(contentLikeService.getLikesByContentIds(List.of(100L), Content.ContentType.MOVIE))
                .thenReturn(Map.of(100L, noLikes()));
        when(watchListMapper.toDtoWithLikes(watchList, 0L, 0L)).thenReturn(dto);

        // When
        List<WatchListResponseDto> result = watchListServiceImpl.getWatchList(
                email, false, Content.ContentType.MOVIE);

        // Then
        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
        verify(watchListRepository, times(1))
                .findByUser_IdAndWatchedAtIsNullAndContent_Type(
                        user.getId(), Content.ContentType.MOVIE);
        verify(watchListRepository, never())
                .findByUser_IdAndContent_Type(any(), any());
    }

    @Test
    @DisplayName("""
            getWatchList | Returns all items when showWatched is true
            """)
    void getWatchList_ShowWatchedTrue_ShouldReturnAll() {
        // Given
        String email = "john@test.com";
        User user = buildUser(1L, email);
        Content content = buildContent(100L, Content.ContentType.MOVIE);
        WatchList watchList = buildWatchList(user, content);
        WatchListResponseDto dto = buildResponseDto(1L);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(watchListRepository.findByUser_IdAndContent_Type(
                user.getId(), Content.ContentType.MOVIE))
                .thenReturn(List.of(watchList));
        when(contentLikeService.getLikesByContentIds(List.of(100L), Content.ContentType.MOVIE))
                .thenReturn(Map.of(100L, noLikes()));
        when(watchListMapper.toDtoWithLikes(watchList, 0L, 0L)).thenReturn(dto);

        // When
        List<WatchListResponseDto> result = watchListServiceImpl.getWatchList(
                email, true, Content.ContentType.MOVIE);

        // Then
        assertEquals(1, result.size());
        verify(watchListRepository, times(1))
                .findByUser_IdAndContent_Type(user.getId(), Content.ContentType.MOVIE);
        verify(watchListRepository, never())
                .findByUser_IdAndWatchedAtIsNullAndContent_Type(any(), any());
    }

    @Test
    @DisplayName("""
            getWatchList | Throws EntityNotFoundException when user not found
            """)
    void getWatchList_UserNotFound_ShouldThrow() {
        // Given
        String email = "notfound@test.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(EntityNotFoundException.class,
                () -> watchListServiceImpl.getWatchList(
                        email, false, Content.ContentType.MOVIE));

        verify(watchListRepository, never()).findByUser_IdAndContent_Type(any(), any());
    }

    // ── addToWatchList ────────────────────────────────────────────────────────

    @Test
    @DisplayName("""
            addToWatchList | Adds content to watchlist successfully
            """)
    void addToWatchList_ValidRequest_ShouldAddAndReturn() {
        // Given
        String email = "john@test.com";
        User user = buildUser(1L, email);
        Content content = buildContent(100L, Content.ContentType.MOVIE);
        WatchList savedWatchList = buildWatchList(user, content);
        WatchListRequestDto requestDto = new WatchListRequestDto(100L, Content.ContentType.MOVIE);
        WatchListResponseDto dto = buildResponseDto(1L);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(watchListRepository.findByUser_IdAndContent_ExternalIdAndContent_Type(
                user.getId(), 100L, Content.ContentType.MOVIE))
                .thenReturn(Optional.empty());
        when(contentService.getOrCreate(100L, Content.ContentType.MOVIE)).thenReturn(content);
        when(watchListRepository.save(any(WatchList.class))).thenReturn(savedWatchList);
        when(contentLikeService.getLikes(100L, Content.ContentType.MOVIE)).thenReturn(noLikes());
        when(watchListMapper.toDtoWithLikes(savedWatchList, 0L, 0L)).thenReturn(dto);

        // When
        WatchListResponseDto result = watchListServiceImpl.addToWatchList(email, requestDto);

        // Then
        assertEquals(dto, result);
        verify(watchListRepository, times(1)).save(any(WatchList.class));
        verify(contentService, times(1)).getOrCreate(100L, Content.ContentType.MOVIE);
    }

    @Test
    @DisplayName("""
            addToWatchList | Throws EntityAlreadyExistsException when content already in watchlist
            """)
    void addToWatchList_AlreadyExists_ShouldThrow() {
        // Given
        String email = "john@test.com";
        User user = buildUser(1L, email);
        WatchListRequestDto requestDto = new WatchListRequestDto(100L, Content.ContentType.MOVIE);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(watchListRepository.findByUser_IdAndContent_ExternalIdAndContent_Type(
                user.getId(), 100L, Content.ContentType.MOVIE))
                .thenReturn(Optional.of(new WatchList()));

        // When / Then
        assertThrows(EntityAlreadyExistsException.class,
                () -> watchListServiceImpl.addToWatchList(email, requestDto));

        verify(watchListRepository, never()).save(any());
        verify(contentService, never()).getOrCreate(any(), any());
    }

    @Test
    @DisplayName("""
            addToWatchList | Throws EntityNotFoundException when user not found
            """)
    void addToWatchList_UserNotFound_ShouldThrow() {
        // Given
        String email = "notfound@test.com";
        WatchListRequestDto requestDto = new WatchListRequestDto(100L, Content.ContentType.MOVIE);

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(EntityNotFoundException.class,
                () -> watchListServiceImpl.addToWatchList(email, requestDto));

        verify(watchListRepository, never()).save(any());
    }

    // ── markAsWatched ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("""
            markAsWatched | Marks content as watched successfully
            """)
    void markAsWatched_ValidRequest_ShouldSetWatchedAt() {
        // Given
        String email = "john@test.com";
        User user = buildUser(1L, email);
        Content content = buildContent(100L, Content.ContentType.MOVIE);
        WatchList watchList = buildWatchList(user, content);
        WatchListRequestDto requestDto = new WatchListRequestDto(100L, Content.ContentType.MOVIE);
        WatchListResponseDto dto = buildResponseDto(1L);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(watchListRepository.findByUser_IdAndContent_ExternalIdAndContent_Type(
                user.getId(), 100L, Content.ContentType.MOVIE))
                .thenReturn(Optional.of(watchList));
        when(watchListRepository.save(watchList)).thenReturn(watchList);
        when(contentLikeService.getLikes(100L, Content.ContentType.MOVIE)).thenReturn(noLikes());
        when(watchListMapper.toDtoWithLikes(watchList, 0L, 0L)).thenReturn(dto);

        // When
        WatchListResponseDto result = watchListServiceImpl.markAsWatched(email, requestDto);

        // Then
        assertEquals(dto, result);
        verify(watchListRepository, times(1)).save(watchList);
    }

    @Test
    @DisplayName("""
            markAsWatched | Throws EntityNotFoundException when watchlist entry not found
            """)
    void markAsWatched_NotInWatchList_ShouldThrow() {
        // Given
        String email = "john@test.com";
        User user = buildUser(1L, email);
        WatchListRequestDto requestDto = new WatchListRequestDto(100L, Content.ContentType.MOVIE);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(watchListRepository.findByUser_IdAndContent_ExternalIdAndContent_Type(
                user.getId(), 100L, Content.ContentType.MOVIE))
                .thenReturn(Optional.empty());

        // When / Then
        assertThrows(EntityNotFoundException.class,
                () -> watchListServiceImpl.markAsWatched(email, requestDto));

        verify(watchListRepository, never()).save(any());
    }

    // ── deleteFromWatchList ───────────────────────────────────────────────────

    @Test
    @DisplayName("""
            deleteFromWatchList | Deletes content from watchlist successfully
            """)
    void deleteFromWatchList_ValidRequest_ShouldDelete() {
        // Given
        String email = "john@test.com";
        User user = buildUser(1L, email);
        Content content = buildContent(100L, Content.ContentType.MOVIE);
        WatchList watchList = buildWatchList(user, content);
        WatchListRequestDto requestDto = new WatchListRequestDto(100L, Content.ContentType.MOVIE);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(watchListRepository.findByUser_IdAndContent_ExternalIdAndContent_Type(
                user.getId(), 100L, Content.ContentType.MOVIE))
                .thenReturn(Optional.of(watchList));

        // When
        watchListServiceImpl.deleteFromWatchList(email, requestDto);

        // Then
        verify(watchListRepository, times(1))
                .delete(watchList);
    }

    @Test
    @DisplayName("""
            deleteFromWatchList | Does nothing when content not in watchlist
            """)
    void deleteFromWatchList_NotInWatchList_ShouldDoNothing() {
        // Given
        String email = "john@test.com";
        User user = buildUser(1L, email);
        WatchListRequestDto requestDto = new WatchListRequestDto(100L, Content.ContentType.MOVIE);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(watchListRepository.findByUser_IdAndContent_ExternalIdAndContent_Type(
                user.getId(), 100L, Content.ContentType.MOVIE))
                .thenReturn(Optional.empty());

        // When
        watchListServiceImpl.deleteFromWatchList(email, requestDto);

        // Then
        verify(watchListRepository, never())
                .delete(any());
    }

    @Test
    @DisplayName("""
            deleteFromWatchList | Throws EntityNotFoundException when user not found
            """)
    void deleteFromWatchList_UserNotFound_ShouldThrow() {
        // Given
        String email = "notfound@test.com";
        WatchListRequestDto requestDto = new WatchListRequestDto(100L, Content.ContentType.MOVIE);

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(EntityNotFoundException.class,
                () -> watchListServiceImpl.deleteFromWatchList(email, requestDto));

        verify(watchListRepository, never())
                .delete(any());
    }
}