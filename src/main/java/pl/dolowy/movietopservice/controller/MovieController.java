package pl.dolowy.movietopservice.controller;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import pl.dolowy.movietopservice.model.Movie;
import pl.dolowy.movietopservice.service.FavouriteMovieService;
import pl.dolowy.movietopservice.service.MovieService;

@Component
@RequiredArgsConstructor
@FxmlView("MovieStage.fxml")
public class MovieController extends AbstractImageControllerTemplate {

    private final MovieService movieService;
    private final FavouriteMovieService favouriteMovieService;
    private final FxControllerAndView<FavouriteMovieController, SplitPane> favouriteMovieControllerSplitPane;

    @FXML
    private Label pickedMovie;

    @FXML
    private TextField movieSearchTextField;

    @FXML
    private Button openFavouriteMoviesButton;

    @Override
    public void initialize() {
        openFavouriteMoviesButton.setOnAction(
                actionEvent -> favouriteMovieControllerSplitPane.getController().show());

        setScrollBar(moviesTableView);

        moviesTableView
                .getSelectionModel()
                .selectedItemProperty()
                .addListener((observableValue, oldValue, newValue) -> {
                    Movie currentMovie = moviesTableView.getSelectionModel().getSelectedItem();
                    pickedMovie.setText(
                            "Your movie: " + currentMovie.getTitle() + " from " + currentMovie.getReleased());

                    addToFavouriteButtonAction(currentMovie, favouriteMovieService);
                });
    }

    @FXML
    public void findMovies() {
        progress = 0;
        Task<List<Movie>> task = new Task<>() {
            @Override
            protected List<Movie> call() {
                return movieService.getMoviesFromApi(movieSearchTextField.getText());
            }
        };

        task.setOnSucceeded(t -> {
            List<Movie> movies = task.getValue();

            if (movies.isEmpty()) {
                pickedMovie.setText("MOVIE NOT FOUND");
            } else {
                pickedMovie.setText("");

                ObservableList<Movie> data = FXCollections.observableList(movies);

                setColumnForTableView(moviesTableView);
                wrapEachColumnsFromTableView();

                moviesTableView.setItems(data);

                setTableViewForImagePoster(movies);
                scroll.setMax(data.size());
            }

            progressBar.setVisible(false);
            progress = 1.0;
        });

        setThreadForProgressBar();

        Thread thread = new Thread(task);
        thread.start();
    }
}
