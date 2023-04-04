package pl.dolowy.movietopservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class Movie {
    @Column(name = "imdb_id")
    private String imdbID;
    private String title;
    private String type;
    private String poster;

    @DateTimeFormat(style = "dd MMM YYYY")
//    @JsonFormat(pattern = "dd MMM YYYY")
//    https://stackoverflow.com/questions/40327970/deserialize-java-8-localdatetime-with-jacksonmapper
    private LocalDate released;
    private String plot;
    private String director;
}
