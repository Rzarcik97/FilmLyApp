package filmly.enums;

import lombok.Getter;

@Getter
public enum SortBy {
    POPULARITY_DESC("popularity.desc"),
    POPULARITY_ASC("popularity.asc"),
    VOTE_AVERAGE_DESC("vote_average.desc"),
    VOTE_AVERAGE_ASC("vote_average.asc"),
    VOTE_COUNT_DESC("vote_count.desc"),
    VOTE_COUNT_ASC("vote_count.asc"),
    RELEASE_DATE_DESC("release_date.desc"),
    RELEASE_DATE_ASC("release_date.asc"),
    TITLE_DESC("title.desc"),
    TITLE_ASC("title.asc");

    private final String value;

    SortBy(String value) {
        this.value = value;
    }
}
