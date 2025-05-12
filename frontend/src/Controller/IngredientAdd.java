package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class IngredientAdd {
    @FXML
    private ImageView exitButton;

    @FXML
    private Button btnEditThongTin;

    @FXML
    private Button btnHuyThongTin;

    @FXML
    private void onExitButtonClick() {

        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void handleEditThongTin(MouseEvent event) {

    }

    @FXML
    public void handleHuyThongTin(MouseEvent event) {

    }


}
