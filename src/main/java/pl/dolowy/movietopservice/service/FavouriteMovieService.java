package pl.dolowy.movietopservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dolowy.movietopservice.model.FavouriteMovie;
import pl.dolowy.movietopservice.model.Movie;
import pl.dolowy.movietopservice.repository.FavouriteMovieRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavouriteMovieService {

    private final FavouriteMovieRepository favouriteMovieRepository;

    public Optional<FavouriteMovie> findFavouriteMovieById(Long id) {
        return favouriteMovieRepository.findById(id);
    }

    public List<FavouriteMovie> findAll() {
        return favouriteMovieRepository.findAll();
    }


    public void delete(Long id) {
        favouriteMovieRepository.deleteById(id);
    }

    public boolean save(Movie movie) {
        FavouriteMovie favouriteMovie = getFavouriteMovie(movie);

        if (favouriteMovieRepository.findAll()
                .stream()
                .map(FavouriteMovie::getImdbID)
                .noneMatch(e -> e.equals(favouriteMovie.getImdbID()))) {
            favouriteMovieRepository.save(favouriteMovie);
            return true;
        }
        return false;
    }

    private static FavouriteMovie getFavouriteMovie(Movie movie) {
        return FavouriteMovie.builder()
                .imdbID(movie.getImdbID()).title(movie.getTitle())
                .type(movie.getType())
                .released(movie.getReleased())
                .director(movie.getDirector())
                .plot(movie.getPlot())
                .poster(movie.getPoster())
                .build();
    }



}
