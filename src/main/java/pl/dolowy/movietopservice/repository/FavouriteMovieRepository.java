package pl.dolowy.movietopservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.dolowy.movietopservice.model.FavouriteMovie;

@Repository
public interface FavouriteMovieRepository extends JpaRepository<FavouriteMovie, Long> {
}
