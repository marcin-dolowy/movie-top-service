package pl.dolowy.movietopservice.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;

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

    public FavouriteMovie(Long id, String imdbID, String title, String type, LocalDate released, String director, String plot, String poster, int rating) {
        super(imdbID, title, type, released, director, plot, poster);
        this.id = id;
        this.rating = rating;
    }
}
