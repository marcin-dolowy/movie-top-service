package pl.dolowy.movietopservice.model;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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
