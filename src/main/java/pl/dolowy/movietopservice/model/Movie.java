package pl.dolowy.movietopservice.model;

import javafx.scene.image.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDate;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class Movie {

    @Column(name = "imdb_id")
    private String imdbID;
    private String title;
    private String type;
    private LocalDate released;
    private String director;
    private String plot;
    private String poster;
}
