package filmly.dto.contentlikes;

public interface ContentLikeCountProjection {

    Long getContentId();

    Long getLikes();

    Long getDislikes();
}
