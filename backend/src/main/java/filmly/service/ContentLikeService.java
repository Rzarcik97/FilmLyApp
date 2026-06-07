package filmly.service;

import filmly.dto.content.ContentDto;
import filmly.dto.contentlikes.ContentLikeRequestDto;
import filmly.dto.contentlikes.ContentLikeResponseDto;
import filmly.model.Content;
import java.util.List;
import java.util.Map;

public interface ContentLikeService {

    List<ContentDto> getLikedContent(String email, Content.ContentType type, Boolean liked);

    ContentLikeResponseDto toggleLike(String email, ContentLikeRequestDto dto);

    ContentLikeResponseDto getLikes(Long contentId, Content.ContentType contentType);

    Map<Long, ContentLikeResponseDto> getLikesByContentIds(
            List<Long> contentIds, Content.ContentType contentType);

    Boolean isLiked(String email, Long contentId, Content.ContentType type);
}
