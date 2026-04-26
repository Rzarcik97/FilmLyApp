package filmly.service.impl;

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
import filmly.service.ContentRatingService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
public class ContentRatingServiceImpl implements ContentRatingService {

    private final ContentRatingRepository contentRatingRepository;
    private final UserRepository userRepository;
    private final ContentRatingMapper contentRatingMapper;

    @Override
    public List<ContentRatingResponseDto> getByContentId(
            Long contentId,
            Content.ContentType contentType) {
        return contentRatingRepository
                .findLatestByContentIdAndContentType(contentId, contentType)
                .stream()
                .map(contentRatingMapper::toDto)
                .toList();
    }

    @Override
    public ContentRatingResponseDto addRating(String email, ContentRatingRequestDto dto) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User", email));
        if (contentRatingRepository.findByUser_IdAndContentIdAndContentType(
                user.getId(), dto.contentId(), dto.contentType()).isPresent()) {
            throw new EntityAlreadyExistsException("Rating already exists for this content");
        }
        ContentRating contentRating = new ContentRating();
        contentRating.setUser(user);
        contentRating.setContentId(dto.contentId());
        contentRating.setContentType(dto.contentType());
        contentRating.setRating(dto.rating());
        contentRating.setReview(dto.review());
        contentRating.setCreatedAt(LocalDateTime.now());
        log.info("User {} added rating {} for {} {}",
                email, dto.rating(), dto.contentType(), dto.contentId());
        return contentRatingMapper.toDto(contentRatingRepository.save(contentRating));
    }

    @Override
    public ContentRatingResponseDto updateRating(String email, ContentRatingUpdateRequestDto dto) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User", email));
        ContentRating contentRating = contentRatingRepository
                .findByUser_IdAndContentIdAndContentType(
                        user.getId(), dto.contentId(), dto.contentType())
                .orElseThrow(() -> new EntityNotFoundException("Rating", dto.contentId()));
        if (dto.rating() != null) {
            contentRating.setRating(dto.rating());
        }
        if (dto.review() != null && !dto.review().isBlank()) {
            contentRating.setReview(dto.review());
        }
        log.info("User {} updated rating to {} for {} {}",
                email, dto.rating(), dto.contentType(), dto.contentId());
        return contentRatingMapper.toDto(contentRatingRepository.save(contentRating));
    }

    @Override
    @Transactional
    public void deleteRating(String email, Long id) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User", email));
        ContentRating contentRating = contentRatingRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Rating", id)
        );
        if (!contentRating.getUser().getId().equals(user.getId())) {
            throw new AuthenticationException("You are not allowed to delete this rating");
        }
        contentRatingRepository.delete(contentRating);
        log.info("User {} deleted rating for {} with id: {}",
                email, contentRating.getContentType(), contentRating.getContentId());
    }
}
