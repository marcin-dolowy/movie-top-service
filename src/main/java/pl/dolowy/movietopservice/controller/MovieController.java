package pl.dolowy.movietopservice.controller;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Duration;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Controller;
import pl.dolowy.movietopservice.model.ImagePoster;
import pl.dolowy.movietopservice.model.Movie;
import pl.dolowy.movietopservice.service.FavouriteMovieService;
import pl.dolowy.movietopservice.service.MovieService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@FxmlView("MainStage.fxml")
public class MovieController {

    private final MovieService movieService;
    private final FavouriteMovieService favouriteMovieService;
    private final FxControllerAndView<FavouriteMovieController, SplitPane> favouriteMovieControllerSplitPane;
    private final FxWeaver fxWeaver;

    @FXML
    private Label infoLabel;
    @FXML
    private Button addToFavouriteButton;
    @FXML
    private Button openFavouriteMoviesButton;
    @FXML
    private TextField movieSearchTextField;
    @FXML
    private TableView<Movie> moviesTableView = new TableView<>();
    @FXML
    private TableColumn<Movie, String> imdbIDTableColumn;
    @FXML
    private TableColumn<Movie, String> titleTableColumn;
    @FXML
    private TableColumn<Movie, String> typeTableColumn;
    @FXML
    private TableColumn<Movie, LocalDate> releasedTableColumn;
    @FXML
    private TableColumn<Movie, String> directorTableColumn;
    @FXML
    private TableColumn<Movie, String> plotTableColumn;
    @FXML
    private Label pickedMovie;
    @FXML
    private TableView<ImagePoster> posterTableView = new TableView<>();
    @FXML
    private TableColumn<ImagePoster, ImageView> posterTableColumn;
    @FXML
    private ScrollBar scroll;

    @FXML
    public void initialize() {
//        openFavouriteMoviesButton.setOnAction(actionEvent ->
//                favouriteMovieControllerSplitPane.getController().show());


        openFavouriteMoviesButton.setOnAction(
                actionEvent -> fxWeaver.loadController(FavouriteMovieController.class).show()
        );

        scroll.setMax(moviesTableView.getFixedCellSize());
        scroll.setMin(0);
        scroll.valueProperty().addListener((observableValue, number, t1) -> {
            moviesTableView.scrollTo(t1.intValue());
            posterTableView.scrollTo(t1.intValue());
        });

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

    private void displayInfoLabel(PauseTransition pauseTransition, String message) {
        infoLabel.setText(message);
        pauseTransition.setOnFinished(e -> infoLabel.setText(""));
        pauseTransition.play();
    }

    @FXML
    void findMovies() {
        List<Movie> movies = movieService.getMoviesFromApi(movieSearchTextField.getText());
        if (movies.isEmpty()) {
            pickedMovie.setText("MOVIE NOT FOUND");
        } else {
            pickedMovie.setText("");

            ObservableList<Movie> data = FXCollections.observableList(movies);

            setColumnForTableView();
            wrapTextForTableColumn(plotTableColumn);
            wrapTextForTableColumn(titleTableColumn);
            wrapTextForTableColumn(directorTableColumn);

            moviesTableView.setItems(data);

            List<ImagePoster> imagesFromMovies = getPostersFromEachMovie(movies);
            ObservableList<ImagePoster> imagePosters = FXCollections.observableList(imagesFromMovies);

            posterTableColumn.setCellValueFactory((new PropertyValueFactory<>("image")));
            posterTableView.setItems(imagePosters);
            scroll.setMax(data.size());
        }

    }

    private void wrapTextForTableColumn(TableColumn<Movie, String> tableColumn) {
        tableColumn.setCellFactory(tc -> {
            TableCell<Movie, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(tableColumn.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });
    }

    private static List<ImagePoster> getPostersFromEachMovie(List<Movie> movies) {
        return movies.stream()
                .map(Movie::getPoster)
                .map(s -> {
                    ImageView imageView = new ImageView(s);
                    imageView.setFitHeight(150);
                    imageView.setFitWidth(180);
                    return new ImagePoster(imageView);
                })
                .collect(Collectors.toList());
    }

    private void setColumnForTableView() {
        imdbIDTableColumn.setCellValueFactory((new PropertyValueFactory<>("imdbID")));
        titleTableColumn.setCellValueFactory((new PropertyValueFactory<>("title")));
        typeTableColumn.setCellValueFactory((new PropertyValueFactory<>("type")));
        releasedTableColumn.setCellValueFactory((new PropertyValueFactory<>("released")));
        directorTableColumn.setCellValueFactory((new PropertyValueFactory<>("director")));
        plotTableColumn.setCellValueFactory((new PropertyValueFactory<>("plot")));
    }

}

//    private void wrapTextForPlotTableColumn() {
//        plotTableColumn.setCellFactory(tc -> {
//            TableCell<Movie, String> cell = new TableCell<>();
//            Text text = new Text();
//            cell.setGraphic(text);
//            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
//            text.wrappingWidthProperty().bind(plotTableColumn.widthProperty());
//            text.textProperty().bind(cell.itemProperty());
//            return cell;
//        });
//    }
