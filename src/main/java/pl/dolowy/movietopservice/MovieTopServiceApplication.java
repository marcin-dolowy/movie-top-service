package pl.dolowy.movietopservice;

import javafx.application.Application;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import pl.dolowy.movietopservice.repository.FavouriteMovieRepository;
import pl.dolowy.movietopservice.service.MovieService;

@SpringBootApplication
public class MovieTopServiceApplication {

    public static void main(String[] args) {
        Application.launch(JavaFxApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx, FavouriteMovieRepository favouriteMovieRepository) {
        return args -> {
            MovieService movieService = new MovieService(favouriteMovieRepository);
            System.out.println(movieService.getMoviesFromApi("A"));
        };
    }
}
