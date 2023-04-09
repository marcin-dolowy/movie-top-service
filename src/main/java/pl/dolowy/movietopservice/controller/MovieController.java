package pl.dolowy.movietopservice.controller;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import pl.dolowy.movietopservice.model.Movie;
import pl.dolowy.movietopservice.service.FavouriteMovieService;
import pl.dolowy.movietopservice.service.MovieService;

import java.util.List;

@Component
@RequiredArgsConstructor
@FxmlView("MovieStage.fxml")
public class MovieController extends AbstractImageController {

    private final MovieService movieService;
    private final FavouriteMovieService favouriteMovieService;
    private final FxControllerAndView<FavouriteMovieController, SplitPane> favouriteMovieControllerSplitPane;

    @FXML
    private TableView<Movie> moviesTableView = new TableView<>();
    @FXML
    private Label pickedMovie;
    @FXML
    private TextField movieSearchTextField;
    @FXML
    private Button openFavouriteMoviesButton;
    @FXML
    private Button addToFavouriteButton;

    @FXML
    public void initialize() {
        openFavouriteMoviesButton.setOnAction(
                actionEvent -> favouriteMovieControllerSplitPane.getController().show());

        setScrollBar();

        moviesTableView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observableValue, oldValue, newValue) -> {
                    Movie currentMovie = moviesTableView.getSelectionModel().getSelectedItem();
                    pickedMovie.setText("Your movie: " + currentMovie.getTitle() + " from " + currentMovie.getReleased());

                    addToFavouriteButton.setOnMouseClicked(
                            mouseEvent -> {
                                PauseTransition pauseTransition = new PauseTransition(Duration.seconds(3));
                                if (favouriteMovieService.add(currentMovie)) {
                                    displayInfoLabel(pauseTransition, "Successfully added");
                                } else {
                                    displayInfoLabel(pauseTransition, "Movie already in favorites");
                                }
                            });
                });

    }

    @FXML
    void findMovies() {
        List<Movie> movies = movieService.getMoviesFromApi(movieSearchTextField.getText());
        if (movies.isEmpty()) {
            pickedMovie.setText("MOVIE NOT FOUND");
        } else {
            pickedMovie.setText("");

            ObservableList<Movie> data = FXCollections.observableList(movies);
            scroll.setMax(data.size());

            setColumnForTableView(moviesTableView);
            wrapEachColumnsFromTableView();

            moviesTableView.setItems(data);

            setTableViewForImagePoster(movies);
        }
    }

}
