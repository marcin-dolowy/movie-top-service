package pl.dolowy.movietopservice.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import org.springframework.stereotype.Component;
import pl.dolowy.movietopservice.model.ImagePoster;
import pl.dolowy.movietopservice.model.Movie;

import java.util.List;
import java.util.stream.Collectors;


@Component
public abstract class AbstractImageControllerTemplate extends AbstractControllerTemplate {

    public static List<ImagePoster> getImagePosters(List<? extends Movie> favouriteMovies) {
        return favouriteMovies.stream()
                .map(Movie::getPoster)
                .map(s -> {
                    ImageView imageView = new ImageView(s);
                    imageView.setFitHeight(150);
                    imageView.maxHeight(150);
                    imageView.setFitWidth(180);
                    return new ImagePoster(imageView);
                })
                .collect(Collectors.toList());
    }

    public void setTableViewForImagePoster(List<? extends Movie> movies) {
        List<ImagePoster> imagesFromMovies = getImagePosters(movies);
        ObservableList<ImagePoster> imagePosters = FXCollections.observableList(imagesFromMovies);

        posterTableColumn.setCellValueFactory((new PropertyValueFactory<>("image")));
        posterTableView.setFixedCellSize(155);
        posterTableView.setItems(imagePosters);
    }

}
