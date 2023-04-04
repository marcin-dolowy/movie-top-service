package pl.dolowy.movietopservice.model;

import lombok.*;
import lombok.AllArgsConstructor;

import javax.persistence.MappedSuperclass;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class Movie {
    private String id;
    private String title;
    private String type;
    private String image;
    private LocalDate releaseDate;
    private String plot;
    private String directors;
}
