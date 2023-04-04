package pl.dolowy.movietopservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import pl.dolowy.movietopservice.exception.MovieNotFoundException;
import pl.dolowy.movietopservice.model.Movie;
import pl.dolowy.movietopservice.repository.FavouriteMovieRepository;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class MovieService {
    public static final String API_KEY = "d3d95a11";

    private final FavouriteMovieRepository favouriteMovieRepository;
    private List<Movie> movies = new ArrayList<>();
    ObjectMapper objectMapper = new ObjectMapper();


    @SneakyThrows
    public List<Movie> saveMovie(String title) {
        title = title.replaceAll(" ", "+");

        JsonNode jsonNode = objectMapper
                .readTree(new URL("http://www.omdbapi.com/?s=" + title + "&apikey=d3d95a11"));

        System.out.println(jsonNode.toPrettyString());

        // TODO Zrobic to lepiej
        if(!jsonNode.get("Response").asBoolean()) {
           return Collections.emptyList();
       }

        JsonNode listOfMovies = jsonNode.get("Search");

        List<JsonNode> idOfMovies = StreamSupport
                .stream(listOfMovies.spliterator(), false)
                .filter(i -> i.has("imdbID"))
                .map(jsonNode1 -> jsonNode1.get("imdbID"))
                .collect(Collectors.toList());

        movies = idOfMovies.stream()
                .map(JsonNode::textValue)
                .map(idMovie -> {
                    try {
                        JsonNode movie = objectMapper
                                .readTree(new URL("http://www.omdbapi.com/?i=" + idMovie + "&plot=full&apikey=" + API_KEY));
                        return fromJsonToMovie(movie);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return movies;
    }

    private static Movie fromJsonToMovie(JsonNode movie) {
        return Movie.builder()
                .imdbID(movie.get("imdbID").textValue())
                .title(movie.get("Title").textValue())
                .type(movie.get("Type").textValue())
                .poster(movie.get("Poster").textValue())
                .released(LocalDate.parse(movie.get("Released").textValue(), getDateTimeFormatter()))
                .plot(movie.get("Plot").textValue())
                .director(movie.get("Director").textValue())
                .build();
    }

    private static DateTimeFormatter getDateTimeFormatter() {
        return new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("dd MMM yyyy")
                .toFormatter(Locale.ENGLISH);
    }

}
