package filmly.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "genres")
public class Genre {
    @Id
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}
