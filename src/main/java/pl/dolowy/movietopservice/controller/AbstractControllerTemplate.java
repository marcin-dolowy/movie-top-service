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
import org.controlsfx.control.Rating;
import org.springframework.stereotype.Component;
import pl.dolowy.movietopservice.model.FavouriteMovie;
import pl.dolowy.movietopservice.model.ImagePoster;
import pl.dolowy.movietopservice.model.Movie;
import pl.dolowy.movietopservice.service.FavouriteMovieService;

import java.time.LocalDate;
import java.util.List;

@Component
public abstract class AbstractControllerTemplate {

    @FXML
    protected TableView<Movie> moviesTableView = new TableView<>();
    @FXML
    protected TableView<FavouriteMovie> favouriteMoviesTableView = new TableView<>();
    @FXML
    protected TableView<ImagePoster> posterTableView = new TableView<>();
    @FXML
    protected TableColumn<ImagePoster, ImageView> posterTableColumn;
    @FXML
    protected TableColumn<?, String> imdbIDTableColumn;
    @FXML
    protected TableColumn<?, String> titleTableColumn;
    @FXML
    protected TableColumn<?, String> typeTableColumn;
    @FXML
    protected TableColumn<?, LocalDate> releasedTableColumn;
    @FXML
    protected TableColumn<?, String> directorTableColumn;
    @FXML
    protected TableColumn<?, String> plotTableColumn;
    @FXML
    protected ScrollBar scroll;
    @FXML
    protected Label infoLabel;
    @FXML
    protected Rating rating;
    @FXML
    protected Button rateButton;
    @FXML
    protected Button deleteMovieButton;
    @FXML
    protected Button addToFavouriteButton;

    @FXML
    public abstract void initialize();

    protected void setScrollBar(TableView<?> tableView) {
        scroll.setMax(tableView.getItems().size());
        scroll.setMin(0);
        scroll.valueProperty().addListener((observableValue, number, t1) -> {
            tableView.scrollTo(t1.intValue());
            posterTableView.scrollTo(t1.intValue());
        });
    }

    protected void setColumnForTableView(TableView<?> tableView) {
        imdbIDTableColumn.setCellValueFactory((new PropertyValueFactory<>("imdbID")));
        titleTableColumn.setCellValueFactory((new PropertyValueFactory<>("title")));
        typeTableColumn.setCellValueFactory((new PropertyValueFactory<>("type")));
        releasedTableColumn.setCellValueFactory((new PropertyValueFactory<>("released")));
        directorTableColumn.setCellValueFactory((new PropertyValueFactory<>("director")));
        plotTableColumn.setCellValueFactory((new PropertyValueFactory<>("plot")));
        tableView.setFixedCellSize(155);
    }

    protected static <T> void wrapTextForTableColumn(TableColumn<T, String> tableColumn) {
        tableColumn.setCellFactory(tc -> {
            TableCell<T, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(tableColumn.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });
    }

    protected void wrapEachColumnsFromTableView() {
        wrapTextForTableColumn(titleTableColumn);
        wrapTextForTableColumn(directorTableColumn);
        wrapTextForTableColumn(plotTableColumn);
    }

    protected void displayInfoLabel(PauseTransition pauseTransition, String message) {
        infoLabel.setText(message);
        pauseTransition.setOnFinished(e -> infoLabel.setText(""));
        pauseTransition.play();
    }

    protected void setUpdatedTableViewAfterDeleteMovie(List<FavouriteMovie> favouriteMovies) {
        ObservableList<FavouriteMovie> data = FXCollections.observableList(favouriteMovies);
        favouriteMoviesTableView.setItems(data);
        List<ImagePoster> imagesFromMovies = AbstractImageControllerTemplate.getImagePosters(favouriteMovies);
        ObservableList<ImagePoster> imagePosters = FXCollections.observableList(imagesFromMovies);
        posterTableView.setItems(imagePosters);
    }

    protected void rateButtonClickAction(FavouriteMovie currentMovie, FavouriteMovieService favouriteMovieService) {
        rateButton.setOnMouseClicked(
                mouseEvent -> {
                    if (currentMovie != null) {
                        currentMovie.setRating((int) rating.getRating());
                        favouriteMovieService.updateFavouriteMovie(currentMovie.getId(), currentMovie);
                        favouriteMoviesTableView.refresh();
                    }
                }
        );
    }

    protected void deleteMovieButtonAction(FavouriteMovie currentMovie, FavouriteMovieService favouriteMovieService) {
        deleteMovieButton.setOnMouseClicked(
                mouseEvent -> {
                    PauseTransition pauseTransition = new PauseTransition(Duration.seconds(3));
                    if (favouriteMovieService.delete(currentMovie.getId())) {
                        displayInfoLabel(pauseTransition, "Successfully deleted");
                        setUpdatedTableViewAfterDeleteMovie(favouriteMovieService.findAll());
                    }
                });
    }

    protected void addToFavouriteButtonAction(Movie currentMovie, FavouriteMovieService favouriteMovieService) {
        addToFavouriteButton.setOnMouseClicked(
                mouseEvent -> {
                    PauseTransition pauseTransition = new PauseTransition(Duration.seconds(3));
                    if (favouriteMovieService.add(currentMovie)) {
                        displayInfoLabel(pauseTransition, "Successfully added");
                    } else {
                        displayInfoLabel(pauseTransition, "Movie already in favorites");
                    }
                });
    }

}
