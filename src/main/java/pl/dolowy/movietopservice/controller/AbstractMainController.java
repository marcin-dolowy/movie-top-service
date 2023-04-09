package pl.dolowy.movietopservice.controller;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.springframework.stereotype.Component;
import pl.dolowy.movietopservice.model.FavouriteMovie;
import pl.dolowy.movietopservice.model.ImagePoster;
import pl.dolowy.movietopservice.model.Movie;

import java.time.LocalDate;
import java.util.List;

@Component
public abstract class AbstractMainController {

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
    public abstract void initialize();

    protected void setScrollBar(TableView<?> tableView) {
        scroll.setMax(tableView.getFixedCellSize());
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
        List<ImagePoster> imagesFromMovies = AbstractImageController.getImagePosters(favouriteMovies);
        ObservableList<ImagePoster> imagePosters = FXCollections.observableList(imagesFromMovies);
        posterTableView.setItems(imagePosters);
    }

}
