package pl.dolowy.movietopservice.service;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import pl.dolowy.movietopservice.model.FavouriteMovie;
import pl.dolowy.movietopservice.model.Movie;
import pl.dolowy.movietopservice.repository.FavouriteMovieRepository;

import java.lang.reflect.Field;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavouriteMovieService {

    private final FavouriteMovieRepository favouriteMovieRepository;

    public Optional<FavouriteMovie> findFavouriteMovieById(Long id) {
        return favouriteMovieRepository.findById(id);
    }

//    public void updateFavouriteMovie(Long id, Map<Object, Object> objectMap) {
//        FavouriteMovie favouriteMovie = favouriteMovieRepository.findById(id)
//                .orElseThrow(NoSuchElementException::new);
//
//        objectMap.forEach((key, value) -> {
//            Field field = ReflectionUtils.findField(FavouriteMovie.class, (String) key);
//            Objects.requireNonNull(field).setAccessible(true);
//            ReflectionUtils.setField(field, favouriteMovie, value);
//        });
//        favouriteMovieRepository.save(favouriteMovie);
//    }

    public void updateFavouriteMovie(Long id, FavouriteMovie favouriteMovie) {
        FavouriteMovie movie = favouriteMovieRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);

        ReflectionUtils.doWithFields(FavouriteMovie.class, field -> {
            field.setAccessible(true);
            Object newValue = field.get(favouriteMovie);
            if (newValue != null) {
                field.set(movie, newValue);
            }
        });
        favouriteMovieRepository.save(movie);
    }

    public List<FavouriteMovie> findAll() {
        return favouriteMovieRepository.findAll();
    }

    public boolean delete(Long id) {
        if (findFavouriteMovieById(id).isPresent()) {
            favouriteMovieRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean add(Movie movie) {
        FavouriteMovie favouriteMovie = getFavouriteMovie(movie);

        if (favouriteMovieRepository.findAll()
                .stream()
                .map(FavouriteMovie::getImdbID)
                .noneMatch(e -> e.equals(favouriteMovie.getImdbID()))) {
            favouriteMovieRepository.save(favouriteMovie);
            log.info("Movie successfully added");
            return true;
        }
        log.info("Selected movie is already on the favorites list");
        return false;
    }

    private static FavouriteMovie getFavouriteMovie(Movie movie) {
        return FavouriteMovie
                .builder()
                .imdbID(movie.getImdbID())
                .title(movie.getTitle())
                .type(movie.getType())
                .released(movie.getReleased())
                .director(movie.getDirector())
                .plot(movie.getPlot())
                .poster(movie.getPoster())
                .build();
    }

}
