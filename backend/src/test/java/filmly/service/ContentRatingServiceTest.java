package filmly.service;

import filmly.dto.contentrating.ContentRatingRequestDto;
import filmly.dto.contentrating.ContentRatingResponseDto;
import filmly.dto.contentrating.ContentRatingUpdateRequestDto;
import filmly.exception.AuthenticationException;
import filmly.exception.EntityAlreadyExistsException;
import filmly.exception.EntityNotFoundException;
import filmly.mapper.ContentRatingMapper;
import filmly.model.Content;
import filmly.model.ContentRating;
import filmly.model.User;
import filmly.repository.ContentRatingRepository;
import filmly.repository.UserRepository;
import filmly.service.impl.ContentRatingServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContentRatingServiceTest {

    @Mock
    private ContentRatingRepository contentRatingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ContentRatingMapper contentRatingMapper;

    @InjectMocks
    private ContentRatingServiceImpl contentRatingService;

    @Test
    @DisplayName("""
            getByContentId | should return ratings list
            """)
    void getByContentId_success() {
        // given
        ContentRating rating = new ContentRating();

        ContentRatingResponseDto dto = mock(ContentRatingResponseDto.class);

        when(contentRatingRepository.findLatestByContentIdAndContentType(
                100L,
                Content.ContentType.MOVIE
        )).thenReturn(List.of(rating));

        when(contentRatingMapper.toDto(rating))
                .thenReturn(dto);

        // when
        List<ContentRatingResponseDto> result =
                contentRatingService.getByContentId(
                        100L,
                        Content.ContentType.MOVIE
                );

        // then
        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));

        verify(contentRatingRepository)
                .findLatestByContentIdAndContentType(
                        100L,
                        Content.ContentType.MOVIE
                );

        verify(contentRatingMapper).toDto(rating);
    }

    @Test
    @DisplayName("""
            addRating | should add rating successfully
            """)
    void addRating_success() {
        // given
        String email = "john@example.com";

        User user = new User();
        user.setId(1L);

        ContentRatingRequestDto request = new ContentRatingRequestDto(
                100L,
                Content.ContentType.MOVIE,
                8f,
                "Great movie"
        );

        ContentRating saved = new ContentRating();
        saved.setId(1L);

        ContentRatingResponseDto responseDto =
                mock(ContentRatingResponseDto.class);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(contentRatingRepository
                .findByUser_IdAndContentIdAndContentType(
                        1L,
                        100L,
                        Content.ContentType.MOVIE
                )).thenReturn(Optional.empty());

        when(contentRatingRepository.save(any(ContentRating.class)))
                .thenReturn(saved);

        when(contentRatingMapper.toDto(saved))
                .thenReturn(responseDto);

        // when
        ContentRatingResponseDto result =
                contentRatingService.addRating(email, request);

        // then
        assertEquals(responseDto, result);

        verify(contentRatingRepository)
                .save(any(ContentRating.class));

        verify(contentRatingMapper)
                .toDto(saved);
    }

    @Test
    @DisplayName("""
            addRating | should throw EntityAlreadyExistsException
            when rating already exists
            """)
    void addRating_alreadyExists() {
        // given
        String email = "john@example.com";

        User user = new User();
        user.setId(1L);

        ContentRating existing = new ContentRating();

        ContentRatingRequestDto request = new ContentRatingRequestDto(
                100L,
                Content.ContentType.MOVIE,
                9f,
                "Awesome"
        );

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(contentRatingRepository
                .findByUser_IdAndContentIdAndContentType(
                        1L,
                        100L,
                        Content.ContentType.MOVIE
                )).thenReturn(Optional.of(existing));

        // when + then
        assertThrows(
                EntityAlreadyExistsException.class,
                () -> contentRatingService.addRating(email, request)
        );

        verify(contentRatingRepository, never())
                .save(any(ContentRating.class));

        verifyNoInteractions(contentRatingMapper);
    }

    @Test
    @DisplayName("""
            updateRating | should update rating and review
            """)
    void updateRating_success() {
        // given
        String email = "john@example.com";

        User user = new User();
        user.setId(1L);

        ContentRating rating = new ContentRating();
        rating.setRating(5f);
        rating.setReview("Old review");

        ContentRatingUpdateRequestDto request =
                new ContentRatingUpdateRequestDto(
                        100L,
                        Content.ContentType.MOVIE,
                        10f,
                        "Updated review"
                );

        ContentRatingResponseDto responseDto =
                mock(ContentRatingResponseDto.class);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(contentRatingRepository
                .findByUser_IdAndContentIdAndContentType(
                        1L,
                        100L,
                        Content.ContentType.MOVIE
                )).thenReturn(Optional.of(rating));

        when(contentRatingRepository.save(rating))
                .thenReturn(rating);

        when(contentRatingMapper.toDto(rating))
                .thenReturn(responseDto);

        // when
        ContentRatingResponseDto result =
                contentRatingService.updateRating(email, request);

        // then
        assertEquals(10, rating.getRating());
        assertEquals("Updated review", rating.getReview());

        assertEquals(responseDto, result);

        verify(contentRatingRepository).save(rating);
        verify(contentRatingMapper).toDto(rating);
    }

    @Test
    @DisplayName("""
            updateRating | should update only rating
            when review is blank
            """)
    void updateRating_onlyRating() {
        // given
        String email = "john@example.com";

        User user = new User();
        user.setId(1L);

        ContentRating rating = new ContentRating();
        rating.setRating(5f);
        rating.setReview("Old review");

        ContentRatingUpdateRequestDto request =
                new ContentRatingUpdateRequestDto(
                        100L,
                        Content.ContentType.MOVIE,
                        7f,
                        " "
                );

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(contentRatingRepository
                .findByUser_IdAndContentIdAndContentType(
                        1L,
                        100L,
                        Content.ContentType.MOVIE
                )).thenReturn(Optional.of(rating));

        when(contentRatingRepository.save(rating))
                .thenReturn(rating);

        when(contentRatingMapper.toDto(rating))
                .thenReturn(mock(ContentRatingResponseDto.class));

        // when
        contentRatingService.updateRating(email, request);

        // then
        assertEquals(7, rating.getRating());
        assertEquals("Old review", rating.getReview());
    }

    @Test
    @DisplayName("""
            updateRating | should throw EntityNotFoundException
            when rating not exists
            """)
    void updateRating_notFound() {
        // given
        String email = "john@example.com";

        User user = new User();
        user.setId(1L);

        ContentRatingUpdateRequestDto request =
                new ContentRatingUpdateRequestDto(
                        100L,
                        Content.ContentType.MOVIE,
                        7f,
                        "Review"
                );

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(contentRatingRepository
                .findByUser_IdAndContentIdAndContentType(
                        1L,
                        100L,
                        Content.ContentType.MOVIE
                )).thenReturn(Optional.empty());

        // when + then
        assertThrows(
                EntityNotFoundException.class,
                () -> contentRatingService.updateRating(email, request)
        );

        verify(contentRatingRepository, never())
                .save(any());
    }

    @Test
    @DisplayName("""
            deleteRating | should delete rating successfully
            """)
    void deleteRating_success() {
        // given
        String email = "john@example.com";

        User user = new User();
        user.setId(1L);

        ContentRating rating = new ContentRating();
        rating.setId(10L);
        rating.setUser(user);
        rating.setContentId(100L);
        rating.setContentType(Content.ContentType.MOVIE);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(contentRatingRepository.findById(10L))
                .thenReturn(Optional.of(rating));

        // when
        contentRatingService.deleteRating(email, 10L);

        // then
        verify(contentRatingRepository).delete(rating);
    }

    @Test
    @DisplayName("""
            deleteRating | should throw AuthenticationException
            when user is not owner
            """)
    void deleteRating_notOwner() {
        // given
        String email = "john@example.com";

        User loggedUser = new User();
        loggedUser.setId(1L);

        User owner = new User();
        owner.setId(2L);

        ContentRating rating = new ContentRating();
        rating.setId(10L);
        rating.setUser(owner);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(loggedUser));

        when(contentRatingRepository.findById(10L))
                .thenReturn(Optional.of(rating));

        // when + then
        assertThrows(
                AuthenticationException.class,
                () -> contentRatingService.deleteRating(email, 10L)
        );

        verify(contentRatingRepository, never()).delete(any());
    }

    @Test
    @DisplayName("""
            deleteRating | should throw EntityNotFoundException
            when rating not exists
            """)
    void deleteRating_ratingNotFound() {
        // given
        String email = "john@example.com";

        User user = new User();
        user.setId(1L);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(contentRatingRepository.findById(10L))
                .thenReturn(Optional.empty());

        // when + then
        assertThrows(
                EntityNotFoundException.class,
                () -> contentRatingService.deleteRating(email, 10L)
        );

        verify(contentRatingRepository, never()).delete(any());
    }

    @Test
    @DisplayName("""
            addRating | should throw EntityNotFoundException
            when user not exists
            """)
    void addRating_userNotFound() {
        // given
        ContentRatingRequestDto request =
                new ContentRatingRequestDto(
                        100L,
                        Content.ContentType.MOVIE,
                        9f,
                        "Review"
                );

        when(userRepository.findByEmail("john@example.com"))
                .thenReturn(Optional.empty());

        // when + then
        assertThrows(
                EntityNotFoundException.class,
                () -> contentRatingService.addRating(
                        "john@example.com",
                        request
                )
        );

        verifyNoInteractions(contentRatingRepository);
    }
}
