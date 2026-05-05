package filmly.service.impl;

import filmly.dto.contentlikes.ContentLikeCountProjection;
import filmly.dto.contentlikes.ContentLikeRequestDto;
import filmly.dto.contentlikes.ContentLikeResponseDto;
import filmly.exception.EntityNotFoundException;
import filmly.model.Content;
import filmly.model.ContentLike;
import filmly.model.User;
import filmly.repository.ContentLikeRepository;
import filmly.repository.UserRepository;
import filmly.service.ContentLikeService;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
public class ContentLikeServiceImpl implements ContentLikeService {

    private final ContentLikeRepository contentLikeRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ContentLikeResponseDto toggleLike(String email, ContentLikeRequestDto dto) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User", email));
        Optional<ContentLike> existing = contentLikeRepository
                .findByUser_IdAndContentIdAndContentType(
                        user.getId(), dto.contentId(), dto.contentType());

        if (existing.isPresent()) {
            ContentLike contentLike = existing.get();
            if (contentLike.getLiked().equals(dto.isLike())) {
                contentLikeRepository.delete(contentLike);
                log.info("User {} removed {} for {} {}",
                        email, dto.isLike() ? "like" : "dislike",
                        dto.contentType(), dto.contentId());
            } else {
                contentLike.setLiked(dto.isLike());
                contentLikeRepository.save(contentLike);
                log.info("User {} changed to {} for {} {}",
                        email, dto.isLike() ? "like" : "dislike",
                        dto.contentType(), dto.contentId());
            }
        } else {
            ContentLike contentLike = new ContentLike();
            contentLike.setUser(user);
            contentLike.setContentId(dto.contentId());
            contentLike.setContentType(dto.contentType());
            contentLike.setLiked(dto.isLike());
            contentLike.setCreatedAt(LocalDateTime.now());
            contentLikeRepository.save(contentLike);
            log.info("User {} added {} for {} {}",
                    email, dto.isLike() ? "like" : "dislike",
                    dto.contentType(), dto.contentId());
        }
        return getLikes(dto.contentId(), dto.contentType());
    }

    @Override
    public ContentLikeResponseDto getLikes(Long contentId, Content.ContentType contentType) {
        Long likes = contentLikeRepository.countByContentIdAndContentTypeAndLiked(
                contentId, contentType, true);
        Long dislikes = contentLikeRepository.countByContentIdAndContentTypeAndLiked(
                contentId, contentType, false);
        return new ContentLikeResponseDto(likes, dislikes);
    }

    @Override
    public Map<Long, ContentLikeResponseDto> getLikesByContentIds(
            List<Long> contentIds, Content.ContentType contentType) {
        if (contentIds.isEmpty()) {
            return new HashMap<>();
        }
        List<ContentLikeCountProjection> projections =
                contentLikeRepository.countLikesAndDislikesByContentIds(contentIds, contentType);
        Map<Long, ContentLikeResponseDto> likesMap = new HashMap<>(projections.stream()
                .collect(Collectors.toMap(
                        ContentLikeCountProjection::getContentId,
                        p -> new ContentLikeResponseDto(p.getLikes(), p.getDislikes())
                )));

        contentIds.forEach(id -> likesMap.putIfAbsent(id, new ContentLikeResponseDto(0L, 0L)));
        return likesMap;
    }
}
