package pl.dolowy.movietopservice.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import pl.dolowy.movietopservice.model.FavouriteMovie;
import pl.dolowy.movietopservice.model.ImagePoster;
import pl.dolowy.movietopservice.model.Movie;
import pl.dolowy.movietopservice.service.FavouriteMovieService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@FxmlView("FavouriteMovie.fxml")
@RequiredArgsConstructor
public class FavouriteMovieController {

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
    private TableView<ImagePoster> posterTableView;
    @FXML
    private TableColumn<ImagePoster, ImageView> posterTableColumn;

    @FXML
    private TableView<FavouriteMovie> favouriteMoviesTableView;
    @FXML
    private TableColumn<FavouriteMovie, String> imdbIDTableColumn;
    @FXML
    private TableColumn<FavouriteMovie, String> titleTableColumn;
    @FXML
    private TableColumn<FavouriteMovie, String> typeTableColumn;
    @FXML
    private TableColumn<FavouriteMovie, String> directorTableColumn;
    @FXML
    private TableColumn<FavouriteMovie, String> plotTableColumn;
    @FXML
    private TableColumn<FavouriteMovie, LocalDate> releasedTableColumn;
    @FXML
    private TableColumn<FavouriteMovie, Integer> ratingTableColumn;
    @FXML
    private ScrollBar scroll;

    @FXML
    public void initialize() {

        if (!isWindowOpen) {
            this.stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(favouriteMovieSplitPane));
            isWindowOpen = true;
        }

        displayFavouriteMovies();

        scroll.setMax(favouriteMoviesTableView.getFixedCellSize());
        scroll.setMin(0);
        scroll.valueProperty().addListener((observableValue, number, t1) -> {
            favouriteMoviesTableView.scrollTo(t1.intValue());
            posterTableView.scrollTo(t1.intValue());
        });

        closeButton.setOnAction(
                actionEvent -> stage.close()
        );
    }

    public void show() {
        stage.show();
    }

    public void displayFavouriteMovies() {
        List<FavouriteMovie> favouriteMovies = favouriteMovieService.findAll();
        ObservableList<FavouriteMovie> data = FXCollections.observableList(favouriteMovies);

        imdbIDTableColumn.setCellValueFactory((new PropertyValueFactory<>("imdbID")));
        titleTableColumn.setCellValueFactory((new PropertyValueFactory<>("title")));
        typeTableColumn.setCellValueFactory((new PropertyValueFactory<>("type")));
        releasedTableColumn.setCellValueFactory((new PropertyValueFactory<>("released")));
        directorTableColumn.setCellValueFactory((new PropertyValueFactory<>("director")));
        plotTableColumn.setCellValueFactory((new PropertyValueFactory<>("plot")));
        ratingTableColumn.setCellValueFactory((new PropertyValueFactory<>("rating")));


        plotTableColumn.setCellFactory(tc -> {
            TableCell<FavouriteMovie, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(plotTableColumn.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });

        favouriteMoviesTableView.setItems(data);

        List<ImagePoster> imagesFromMovies = favouriteMovies.stream()
                .map(Movie::getPoster)
                .map(s -> {
                    ImageView imageView = new ImageView(s);
                    imageView.setFitHeight(150);
                    imageView.setFitWidth(180);
                    return new ImagePoster(imageView);
                })
                .collect(Collectors.toList());

        ObservableList<ImagePoster> imagePosters = FXCollections.observableList(imagesFromMovies);

        posterTableColumn.setCellValueFactory((new PropertyValueFactory<>("image")));
        posterTableView.setItems(imagePosters);
        scroll.setMax(data.size());

    }

}
