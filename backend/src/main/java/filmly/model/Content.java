package filmly.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "content", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"external_id", "type"})
})
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long externalId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContentType type;

    @Column(nullable = false)
    private String title;

    @Column(name = "poster_path")
    private String posterPath;

    @Column(name = "release_date")
    private String releaseDate;

    @Column(name = "vote_average")
    private Double voteAverage;

    @Column(name = "vote_count")
    private Integer voteCount;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "content_genres",
            joinColumns = @JoinColumn(name = "content_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres = new ArrayList<>();

    public enum ContentType {
        MOVIE,
        SERIES
    }
}
