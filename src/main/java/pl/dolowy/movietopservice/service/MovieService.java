package pl.dolowy.movietopservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dolowy.movietopservice.repository.FavouriteMovieRepository;

@Service
@RequiredArgsConstructor
public class MovieService {
    public static final String API_KEY = "k_490xy650";

    private final FavouriteMovieRepository favouriteMovieRepository;

}
