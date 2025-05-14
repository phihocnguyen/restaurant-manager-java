package Controller;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class StaffAdd {
    @FXML
    private ImageView exitButton;
    @FXML
    private void onExitButtonClick() {

        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();

    }
}
