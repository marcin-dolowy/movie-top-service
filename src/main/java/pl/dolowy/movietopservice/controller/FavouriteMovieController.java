package pl.dolowy.movietopservice.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Controller;

@Controller
@FxmlView("FavouriteMovie.fxml")
public class FavouriteMovieController {

    private Stage stage;
    boolean isWindowOpen = false;


    @FXML
    private Button closeButton;

    @FXML
    private SplitPane favouriteMovieSplitPane;

    @FXML
    public void initialize() {

        if(!isWindowOpen) {
            this.stage = new Stage();
            stage.setTitle("Your Favourite Movies");
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
