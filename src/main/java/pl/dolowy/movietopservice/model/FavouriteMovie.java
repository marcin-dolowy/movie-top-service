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
    Long movieId;

    public FavouriteMovie(Long movieId, String id, String title, String type, String image, LocalDate releaseDate, String plot, String directors) {
        super(id, title, type, image, releaseDate, plot, directors);
        this.movieId = movieId;
    }
}
