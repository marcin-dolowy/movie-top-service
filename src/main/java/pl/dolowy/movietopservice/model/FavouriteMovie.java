package pl.dolowy.movietopservice.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
public class FavouriteMovie extends Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    public FavouriteMovie(Long id, String imdbID, String title, String type, LocalDate released, String director, String plot, String poster) {
        super(imdbID, title, type, released, director, plot, poster);
        this.id = id;
    }
}
