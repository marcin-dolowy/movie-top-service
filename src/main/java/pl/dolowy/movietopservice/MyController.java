package pl.dolowy.movietopservice;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Controller;

@Controller
@FxmlView("main-stage.fxml")
public class MyController {

    @FXML
    public CheckBox checkBox;
    @FXML
    private Label nameLabel;

    @FXML
    public void showMyName() {
        nameLabel.setText("Witaj, Marcin Dołowy");
    }


}
