package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.Optional;

public class ConfirmDeletedController {

    @FXML
    private Button btnHuy;

    @FXML
    private void handleHuy() {
            Stage stage = (Stage) btnHuy.getScene().getWindow();
            stage.close();
    }
}
