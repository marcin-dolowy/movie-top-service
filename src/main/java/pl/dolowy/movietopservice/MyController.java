package pl.dolowy.movietopservice;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Component
@FxmlView("main-stage.fxml")
public class MyController {

    @FXML
    private Label nameLabel;

    @FXML
    public void showMyName() {
        nameLabel.setText("Witaj, Marcin Dołowy");
    }
}
