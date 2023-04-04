package pl.dolowy.movietopservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
@Data
public class FavouriteMovie extends Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    public FavouriteMovie(Long id, String imdbID, String title, String type, String poster, LocalDate releaseDate, String plot, String director) {
        super(imdbID, title, type, poster, releaseDate, plot, director);
        this.id = id;
    }
}
