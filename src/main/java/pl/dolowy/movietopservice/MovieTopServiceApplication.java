package pl.dolowy.movietopservice;

import javafx.application.Application;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.spring.SpringFxWeaver;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import pl.dolowy.movietopservice.application.JavaFxApplication;
import pl.dolowy.movietopservice.model.Movie;
import pl.dolowy.movietopservice.service.MovieService;

import java.util.List;

@SpringBootApplication
public class MovieTopServiceApplication {

    public static void main(String[] args) {
        Application.launch(JavaFxApplication.class, args);
    }

    @Bean
    public FxWeaver fxWeaver(ConfigurableApplicationContext applicationContext) {
        // Would also work with javafx-weaver-core only:
        // return new FxWeaver(applicationContext::getBean, applicationContext::close);
        return new SpringFxWeaver(applicationContext); //(2)
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            MovieService movieService = new MovieService();
            List<Movie> movies = movieService.getMoviesFromApi("Harry Potter");
            System.out.println(movies.size());
            movies.forEach(System.out::println);
        };
    }
}
