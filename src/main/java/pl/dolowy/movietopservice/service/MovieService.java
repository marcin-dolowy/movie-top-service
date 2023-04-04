package pl.dolowy.movietopservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
@Slf4j
public class MovieService {
    public static final String API_KEY = "d3d95a11";

    private final FavouriteMovieRepository favouriteMovieRepository;
    ObjectMapper objectMapper = new ObjectMapper();


    @SneakyThrows
    public List<Movie> getMoviesFromApi(String title) {
        title = title.replaceAll(" ", "+");

        JsonNode jsonNode = objectMapper
                .readTree(new URL("http://www.omdbapi.com/?s=" + title + "&apikey=d3d95a11"));

        if (jsonNode.get("Response").textValue().equals("False")) {
            log.info("Movies not found");
            return Collections.emptyList();
        }

        List<JsonNode> idOfMovies = getImdbIDFromMoviesList(jsonNode.get("Search"));

        return getMovieByImdbIds(idOfMovies);

    }

    private List<Movie> getMovieByImdbIds(List<JsonNode> idOfMovies) {
        return idOfMovies.stream()
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
    }

    private static List<JsonNode> getImdbIDFromMoviesList(JsonNode listOfMovies) {
        return StreamSupport
                .stream(listOfMovies.spliterator(), false)
                .map(jsonNode1 -> jsonNode1.get("imdbID"))
                .collect(Collectors.toList());
    }

    private static Movie fromJsonToMovie(JsonNode movie) {

        Optional<LocalDate> date = parseValidReleaseDate(movie);

        return Movie.builder()
                .imdbID(movie.get("imdbID").textValue())
                .title(movie.get("Title").textValue())
                .type(movie.get("Type").textValue())
                .poster(movie.get("Poster").textValue())
                .released(date.orElse(null))
                .plot(movie.get("Plot").textValue())
                .director(movie.get("Director").textValue())
                .build();
    }

    private static Optional<LocalDate> parseValidReleaseDate(JsonNode movie) {
        String released = movie.get("Released").textValue();
        return Optional.ofNullable(released)
                .filter(s -> !s.equalsIgnoreCase("N/A"))
                .map(s -> LocalDate.parse(s, getDateTimeFormatter()));
    }

    private static DateTimeFormatter getDateTimeFormatter() {
        return new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("dd MMM yyyy")
                .toFormatter(Locale.ENGLISH);
    }

}
