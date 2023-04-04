package pl.dolowy.movietopservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import pl.dolowy.movietopservice.model.Movie;

import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class MovieService {
    public static final String API_KEY = "k_490xy650";

//    private final FavouriteMovieRepository favouriteMovieRepository;

    private List<Movie> movies = new ArrayList<>();

    @SneakyThrows
    public void saveMovie(String title) {
        title = title.replaceAll(" ", "%20");

        URL url = new URL("https://imdb-api.com/en/API/SearchMovie/" + API_KEY + "/" + title);
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readTree(url);

        JsonNode listOfMovies = jsonNode.get("results");

        StreamSupport.stream(listOfMovies.spliterator(), false)
                .map(jsonNode1 -> jsonNode1.get("title"))
                .forEach(System.out::println);


    }

    @SneakyThrows
    private static HttpResponse<String> getResponse(String apiURL) {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest
                .newBuilder(URI.create(apiURL))
                .build();

        return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

}
