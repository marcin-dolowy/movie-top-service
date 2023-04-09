package pl.dolowy.movietopservice.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@ToString(callSuper = true)
public class FavouriteMovie extends Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "integer default 0")
    private int rating;

    public FavouriteMovie(Long id, String imdbID, String title, String type, LocalDate released, String director, String plot, String poster) {
        super(imdbID, title, type, released, director, plot, poster);
        this.id = id;
    }
}
