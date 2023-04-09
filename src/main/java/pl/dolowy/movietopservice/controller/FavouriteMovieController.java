package pl.dolowy.movietopservice.controller;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxmlView;
import org.controlsfx.control.Rating;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import pl.dolowy.movietopservice.model.FavouriteMovie;
import pl.dolowy.movietopservice.service.FavouriteMovieService;

import java.util.List;

@Controller
@FxmlView("FavouriteMovieStage.fxml")
@RequiredArgsConstructor
public class FavouriteMovieController extends AbstractImageController {

    private Stage stage;
    boolean isWindowOpen = false;
    private final FavouriteMovieService favouriteMovieService;

    @Value("${favouriteMovieStageTitle}")
    private String title;

    @FXML
    private Button closeButton;
    @FXML
    private SplitPane favouriteMovieSplitPane;
    @FXML
    private TableColumn<FavouriteMovie, Integer> ratingTableColumn;
    @FXML
    private Rating rating;
    @FXML
    private Button rateButton;
    @FXML
    private Button deleteMovieButton;


    @FXML
    @Override
    public void initialize() {

        if (!isWindowOpen) {
            this.stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(favouriteMovieSplitPane));
            isWindowOpen = true;
        }

        displayFavouriteMovies();

//        setScrollBar(favouriteMoviesTableView);
        scroll.setMax(favouriteMovieService.findAll().size());
        scroll.setMin(0);
        scroll.valueProperty().addListener((observableValue, number, t1) -> {
            favouriteMoviesTableView.scrollTo(t1.intValue());
            posterTableView.scrollTo(t1.intValue());
        });

        favouriteMoviesTableView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observableValue, oldValue, newValue) -> {
                    FavouriteMovie currentMovie = favouriteMoviesTableView.getSelectionModel().getSelectedItem();

                    rateButton.setOnMouseClicked(
                            mouseEvent -> {
                                if (currentMovie != null) {
                                    currentMovie.setRating((int) rating.getRating());
                                    favouriteMovieService.updateFavouriteMovie(currentMovie.getId(), currentMovie);
                                    favouriteMoviesTableView.refresh();
                                }
                            }
                    );

                    deleteMovieButton.setOnMouseClicked(
                            mouseEvent -> {
                                PauseTransition pauseTransition = new PauseTransition(Duration.seconds(3));
                                if (favouriteMovieService.delete(currentMovie.getId())) {
                                    displayInfoLabel(pauseTransition, "Successfully deleted");
                                    setUpdatedTableViewAfterDeleteMovie(favouriteMovieService.findAll());
                                }
                            });
                });

        closeButton.setOnAction(
                actionEvent -> stage.hide()
        );
    }

    public void show() {
        stage.show();
    }

    public void displayFavouriteMovies() {
        List<FavouriteMovie> favouriteMovies = favouriteMovieService.findAll();
        ObservableList<FavouriteMovie> data = FXCollections.observableList(favouriteMovies);

        setColumnForTableView(favouriteMoviesTableView);
        ratingTableColumn.setCellValueFactory((new PropertyValueFactory<>("rating")));

        wrapEachColumnsFromTableView();

        favouriteMoviesTableView.setItems(data);
        ratingTableColumn.setCellValueFactory((new PropertyValueFactory<>("rating")));

        setTableViewForImagePoster(favouriteMovies);
        scroll.setMax(data.size());

    }

}
