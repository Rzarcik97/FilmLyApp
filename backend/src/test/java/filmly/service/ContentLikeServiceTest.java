package filmly.service;

import filmly.dto.content.ContentDto;
import filmly.dto.contentlikes.ContentLikeRequestDto;
import filmly.dto.contentlikes.ContentLikeResponseDto;
import filmly.exception.EntityNotFoundException;
import filmly.mapper.ContentLikeMapper;
import filmly.model.Content;
import filmly.model.ContentLike;
import filmly.model.User;
import filmly.repository.ContentLikeRepository;
import filmly.repository.UserRepository;
import filmly.service.impl.ContentLikeServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContentLikeServiceTest {

    @Mock
    private ContentLikeRepository contentLikeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ContentLikeMapper contentLikeMapper;

    @Mock
    private ContentService contentService;

    @InjectMocks
    private ContentLikeServiceImpl contentLikeService;

    @Test
    @DisplayName("""
            getLikedContent | should return liked content list
            """)
    void getLikedContent_success() {
        // given
        String email = "john@example.com";

        User user = new User();
        user.setId(1L);
        user.setEmail(email);

        Content content = new Content();
        content.setExternalId(100L);
        content.setType(Content.ContentType.MOVIE);

        ContentLike contentLike = new ContentLike();
        contentLike.setContent(content);
        contentLike.setLiked(true);

        ContentDto dto = mock(ContentDto.class);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(contentLikeRepository.findByUser_IdAndContent_TypeAndLiked(
                1L,
                Content.ContentType.MOVIE,
                true
        )).thenReturn(List.of(contentLike));

        when(contentLikeRepository.countLikesAndDislikesByContentIds(
                List.of(100L),
                Content.ContentType.MOVIE
        )).thenReturn(List.of());

        when(contentLikeMapper.toDto(contentLike, 0L, 0L))
                .thenReturn(dto);

        // when
        List<ContentDto> result = contentLikeService.getLikedContent(
                email,
                Content.ContentType.MOVIE,
                true
        );

        // then
        assertEquals(1, result.size());
        assertEquals(dto, result.getFirst());

        verify(userRepository).findByEmail(email);
        verify(contentLikeRepository)
                .findByUser_IdAndContent_TypeAndLiked(
                        1L,
                        Content.ContentType.MOVIE,
                        true
                );

        verify(contentLikeMapper)
                .toDto(contentLike, 0L, 0L);
    }

    @Test
    @DisplayName("""
            toggleLike | should add new like
            """)
    void toggleLike_addLike() {
        // given
        String email = "john@example.com";

        User user = new User();
        user.setId(1L);

        Content content = new Content();
        content.setExternalId(100L);
        content.setType(Content.ContentType.MOVIE);

        ContentLikeRequestDto request = new ContentLikeRequestDto(
                100L,
                Content.ContentType.MOVIE,
                true
        );

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(contentLikeRepository
                .findByUser_IdAndContent_ExternalIdAndContent_Type(
                        1L,
                        100L,
                        Content.ContentType.MOVIE
                )).thenReturn(Optional.empty());

        when(contentService.getOrCreate(
                100L,
                Content.ContentType.MOVIE
        )).thenReturn(content);

        when(contentLikeRepository
                .countByContent_ExternalIdAndContent_TypeAndLiked(
                        100L,
                        Content.ContentType.MOVIE,
                        true
                )).thenReturn(1L);

        when(contentLikeRepository
                .countByContent_ExternalIdAndContent_TypeAndLiked(
                        100L,
                        Content.ContentType.MOVIE,
                        false
                )).thenReturn(0L);

        // when
        ContentLikeResponseDto result =
                contentLikeService.toggleLike(email, request);

        // then
        assertEquals(1L, result.likes());
        assertEquals(0L, result.dislikes());

        verify(contentService)
                .getOrCreate(100L, Content.ContentType.MOVIE);

        verify(contentLikeRepository).save(any(ContentLike.class));
    }

    @Test
    @DisplayName("""
            toggleLike | should remove existing like
            """)
    void toggleLike_removeExistingLike() {
        // given
        String email = "john@example.com";

        User user = new User();
        user.setId(1L);

        Content content = new Content();
        content.setExternalId(100L);
        content.setType(Content.ContentType.MOVIE);

        ContentLike existing = new ContentLike();
        existing.setLiked(true);
        existing.setContent(content);

        ContentLikeRequestDto request = new ContentLikeRequestDto(
                100L,
                Content.ContentType.MOVIE,
                true
        );

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(contentLikeRepository
                .findByUser_IdAndContent_ExternalIdAndContent_Type(
                        1L,
                        100L,
                        Content.ContentType.MOVIE
                )).thenReturn(Optional.of(existing));

        when(contentLikeRepository
                .countByContent_ExternalIdAndContent_TypeAndLiked(
                        100L,
                        Content.ContentType.MOVIE,
                        true
                )).thenReturn(0L);

        when(contentLikeRepository
                .countByContent_ExternalIdAndContent_TypeAndLiked(
                        100L,
                        Content.ContentType.MOVIE,
                        false
                )).thenReturn(0L);

        // when
        ContentLikeResponseDto result =
                contentLikeService.toggleLike(email, request);

        // then
        assertEquals(0L, result.likes());
        assertEquals(0L, result.dislikes());

        verify(contentLikeRepository).delete(existing);
        verify(contentLikeRepository, never())
                .save(existing);
    }

    @Test
    @DisplayName("""
            toggleLike | should switch dislike to like
            """)
    void toggleLike_switchReaction() {
        // given
        String email = "john@example.com";

        User user = new User();
        user.setId(1L);

        Content content = new Content();
        content.setExternalId(100L);
        content.setType(Content.ContentType.MOVIE);

        ContentLike existing = new ContentLike();
        existing.setLiked(false);
        existing.setContent(content);

        ContentLikeRequestDto request = new ContentLikeRequestDto(
                100L,
                Content.ContentType.MOVIE,
                true
        );

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(contentLikeRepository
                .findByUser_IdAndContent_ExternalIdAndContent_Type(
                        1L,
                        100L,
                        Content.ContentType.MOVIE
                )).thenReturn(Optional.of(existing));

        when(contentLikeRepository
                .countByContent_ExternalIdAndContent_TypeAndLiked(
                        100L,
                        Content.ContentType.MOVIE,
                        true
                )).thenReturn(1L);

        when(contentLikeRepository
                .countByContent_ExternalIdAndContent_TypeAndLiked(
                        100L,
                        Content.ContentType.MOVIE,
                        false
                )).thenReturn(0L);

        // when
        ContentLikeResponseDto result =
                contentLikeService.toggleLike(email, request);

        // then
        assertTrue(existing.getLiked());

        assertEquals(1L, result.likes());
        assertEquals(0L, result.dislikes());

        verify(contentLikeRepository).save(existing);
    }

    @Test
    @DisplayName("""
            getLikes | should return likes and dislikes count
            """)
    void getLikes_success() {
        // given
        when(contentLikeRepository
                .countByContent_ExternalIdAndContent_TypeAndLiked(
                        100L,
                        Content.ContentType.MOVIE,
                        true
                )).thenReturn(5L);

        when(contentLikeRepository
                .countByContent_ExternalIdAndContent_TypeAndLiked(
                        100L,
                        Content.ContentType.MOVIE,
                        false
                )).thenReturn(2L);

        // when
        ContentLikeResponseDto result =
                contentLikeService.getLikes(
                        100L,
                        Content.ContentType.MOVIE
                );

        // then
        assertEquals(5L, result.likes());
        assertEquals(2L, result.dislikes());
    }

    @Test
    @DisplayName("""
            getLikesByContentIds | should return empty map
            when ids list empty
            """)
    void getLikesByContentIds_emptyList() {
        // when
        Map<Long, ContentLikeResponseDto> result =
                contentLikeService.getLikesByContentIds(
                        List.of(),
                        Content.ContentType.MOVIE
                );

        // then
        assertTrue(result.isEmpty());

        verifyNoInteractions(contentLikeRepository);
    }

    @Test
    @DisplayName("""
            isLiked | should return true when content liked
            """)
    void isLiked_true() {
        // given
        String email = "john@example.com";

        User user = new User();
        user.setId(1L);

        ContentLike contentLike = new ContentLike();
        contentLike.setLiked(true);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(contentLikeRepository
                .findByUser_IdAndContent_ExternalIdAndContent_Type(
                        1L,
                        100L,
                        Content.ContentType.MOVIE
                )).thenReturn(Optional.of(contentLike));

        // when
        Boolean result = contentLikeService.isLiked(
                email,
                100L,
                Content.ContentType.MOVIE
        );

        // then
        assertTrue(result);
    }

    @Test
    @DisplayName("""
            isLiked | should return null when reaction not exists
            """)
    void isLiked_null() {
        // given
        String email = "john@example.com";

        User user = new User();
        user.setId(1L);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(contentLikeRepository
                .findByUser_IdAndContent_ExternalIdAndContent_Type(
                        1L,
                        100L,
                        Content.ContentType.MOVIE
                )).thenReturn(Optional.empty());

        // when
        Boolean result = contentLikeService.isLiked(
                email,
                100L,
                Content.ContentType.MOVIE
        );

        // then
        assertNull(result);
    }

    @Test
    @DisplayName("""
            getLikedContent | should throw EntityNotFoundException
            when user not exists
            """)
    void getLikedContent_userNotFound() {
        // given
        when(userRepository.findByEmail("john@example.com"))
                .thenReturn(Optional.empty());

        // when + then
        assertThrows(
                EntityNotFoundException.class,
                () -> contentLikeService.getLikedContent(
                        "john@example.com",
                        Content.ContentType.MOVIE,
                        true
                )
        );

        verify(userRepository).findByEmail("john@example.com");
        verifyNoInteractions(contentLikeRepository);
    }
}