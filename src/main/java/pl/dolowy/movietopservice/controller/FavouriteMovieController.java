package pl.dolowy.movietopservice.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

@Controller
@FxmlView("FavouriteMovie.fxml")
public class FavouriteMovieController {

    private Stage stage;
    boolean isWindowOpen = false;

    @Value("${favouriteMovieStageTitle}")
    private String title;

    @FXML
    private Button closeButton;

    @FXML
    private SplitPane favouriteMovieSplitPane;

    @FXML
    public void initialize() {

        if(!isWindowOpen) {
            this.stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(favouriteMovieSplitPane));
            isWindowOpen = true;
        }

        closeButton.setOnAction(
                actionEvent -> stage.close()
        );
    }
    public void show() {
        stage.show();
    }

}
