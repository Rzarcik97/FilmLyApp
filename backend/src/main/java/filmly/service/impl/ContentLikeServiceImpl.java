package filmly.service.impl;

import filmly.dto.content.ContentDto;
import filmly.dto.contentlikes.ContentLikeCountProjection;
import filmly.dto.contentlikes.ContentLikeRequestDto;
import filmly.dto.contentlikes.ContentLikeResponseDto;
import filmly.exception.EntityNotFoundException;
import filmly.mapper.ContentLikeMapper;
import filmly.model.Content;
import filmly.model.ContentLike;
import filmly.model.User;
import filmly.repository.ContentLikeRepository;
import filmly.repository.UserRepository;
import filmly.service.ContentLikeService;
import filmly.service.ContentService;
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
    private final ContentLikeMapper contentLikeMapper;
    private final ContentService contentService;

    @Override
    public List<ContentDto> getLikedContent(String email, Content.ContentType type, Boolean liked) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User", email));

        List<ContentLike> results = contentLikeRepository
                .findByUser_IdAndContent_TypeAndLiked(user.getId(), type, liked);

        List<Long> contentIds = results.stream()
                .map((c) -> c.getContent().getExternalId())
                .toList();

        Map<Long, ContentLikeResponseDto> likesMap = getLikesByContentIds(
                contentIds, type);

        return results.stream()
                .map(r -> {
                    ContentLikeResponseDto likes = likesMap.get(r.getContent().getExternalId());
                    return contentLikeMapper.toDto(r, likes.likes(),likes.dislikes());
                })
                .toList();
    }

    @Override
    @Transactional
    public ContentLikeResponseDto toggleLike(String email, ContentLikeRequestDto requestDto) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User", email));
        Optional<ContentLike> existing = contentLikeRepository
                .findByUser_IdAndContent_ExternalIdAndContent_Type(
                        user.getId(), requestDto.contentId(), requestDto.contentType());

        if (existing.isPresent()) {
            ContentLike contentLike = existing.get();
            if (contentLike.getLiked().equals(requestDto.isLike())) {
                contentLikeRepository.delete(contentLike);
                log.info("User {} removed {} for {} {}",
                        email, requestDto.isLike() ? "like" : "dislike",
                        requestDto.contentType(), requestDto.contentId());
            } else {
                contentLike.setLiked(requestDto.isLike());
                contentLikeRepository.save(contentLike);
                log.info("User {} changed to {} for {} {}",
                        email, requestDto.isLike() ? "like" : "dislike",
                        requestDto.contentType(), requestDto.contentId());
            }
        } else {
            Content content = contentService.getOrCreate(
                    requestDto.contentId(),
                    requestDto.contentType());
            ContentLike contentLike = new ContentLike();
            contentLike.setUser(user);
            contentLike.setContent(content);
            contentLike.setLiked(requestDto.isLike());
            contentLike.setCreatedAt(LocalDateTime.now());
            contentLikeRepository.save(contentLike);
            log.info("User {} added {} for {} {}",
                    email, requestDto.isLike() ? "like" : "dislike",
                    requestDto.contentType(), requestDto.contentId());
        }
        return getLikes(requestDto.contentId(), requestDto.contentType());
    }

    @Override
    public ContentLikeResponseDto getLikes(Long contentId, Content.ContentType contentType) {
        Long likes = contentLikeRepository.countByContent_ExternalIdAndContent_TypeAndLiked(
                contentId, contentType, true);
        Long dislikes = contentLikeRepository.countByContent_ExternalIdAndContent_TypeAndLiked(
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

    @Override
    public Boolean isLiked(String email, Long contentId, Content.ContentType type) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User", email));
        return contentLikeRepository
                .findByUser_IdAndContent_ExternalIdAndContent_Type(user.getId(), contentId, type)
                .map(ContentLike::getLiked)
                .orElse(null);
    }
}
