package pl.dolowy.movietopservice;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Controller;
import pl.dolowy.movietopservice.model.Movie;
import pl.dolowy.movietopservice.service.MovieService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@FxmlView("main-stage.fxml")
public class MyController {

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
    void findMovies() {
        List<Movie> movies = movieService.getMoviesFromApi(movieSearchTextField.getText());

        List<Image> imagesFromMovies = movies.stream().map(Movie::getPoster)
                .map(Image::new)
                .collect(Collectors.toList());

        ObservableList<Movie> data = FXCollections.observableList(movies);

        imdbIDTableColumn.setCellValueFactory((new PropertyValueFactory<>("imdbID")));
        titleTableColumn.setCellValueFactory((new PropertyValueFactory<>("title")));
        typeTableColumn.setCellValueFactory((new PropertyValueFactory<>("type")));
        releasedTableColumn.setCellValueFactory((new PropertyValueFactory<>("released")));
        directorTableColumn.setCellValueFactory((new PropertyValueFactory<>("director")));
        plotTableColumn.setCellValueFactory((new PropertyValueFactory<>("plot")));

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
    }

}
