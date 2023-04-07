package pl.dolowy.movietopservice.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import pl.dolowy.movietopservice.model.ImagePoster;
import pl.dolowy.movietopservice.model.Movie;
import pl.dolowy.movietopservice.service.MovieService;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@FxmlView("main-stage.fxml")
public class MovieController implements Initializable {

    private final MovieService movieService;

    @FXML
    private TextField movieSearchTextField;

    @FXML
    private Button searchButton;

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
    void findMovies() {

        List<Movie> movies = movieService.getMoviesFromApi(movieSearchTextField.getText());
        ObservableList<Movie> data = FXCollections.observableList(movies);

        setColumnForTableView();

        // Cell Factory to wrap text for plot
        plotTableColumn.setCellFactory(tc -> {
            TableCell<Movie, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(plotTableColumn.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });

        moviesTableView.setItems(data);
        pickedMovie.setText("");


        List<ImagePoster> imagesFromMovies = movies.stream().map(Movie::getPoster)
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

    private void setColumnForTableView() {
        imdbIDTableColumn.setCellValueFactory((new PropertyValueFactory<>("imdbID")));
        titleTableColumn.setCellValueFactory((new PropertyValueFactory<>("title")));
        typeTableColumn.setCellValueFactory((new PropertyValueFactory<>("type")));
        releasedTableColumn.setCellValueFactory((new PropertyValueFactory<>("released")));
        directorTableColumn.setCellValueFactory((new PropertyValueFactory<>("director")));
        plotTableColumn.setCellValueFactory((new PropertyValueFactory<>("plot")));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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
                });
    }
}
