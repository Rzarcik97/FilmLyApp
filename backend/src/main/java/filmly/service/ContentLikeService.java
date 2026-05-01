package filmly.service;

import filmly.dto.contentlikes.ContentLikeRequestDto;
import filmly.dto.contentlikes.ContentLikeResponseDto;
import filmly.model.Content;
import java.util.List;
import java.util.Map;

public interface ContentLikeService {

    void toggleLike(String email, ContentLikeRequestDto dto);

    ContentLikeResponseDto getLikes(Long contentId, Content.ContentType contentType);

    Map<Long, ContentLikeResponseDto> getLikesByContentIds(
            List<Long> contentIds, Content.ContentType contentType);
}
