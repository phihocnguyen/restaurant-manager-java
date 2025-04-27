package Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.Optional;

public class ManagerBills {
    @FXML
    private AnchorPane anchorpane;

    @FXML
    private ImageView exitButtonMenu;

    @FXML
    private ImageView exitButtonMenu1;

    @FXML
    private HBox headerContainer;

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            Stage stage = (Stage) anchorpane.getScene().getWindow();
            stage.setMaximized(true);
        });

    }
    @FXML
    public void onExitButtonClick() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận thoát");
        alert.setHeaderText("Bạn có chắc chắn muốn thoát?");
        alert.setContentText("Nhấn OK để thoát, Cancel để hủy.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) exitButtonMenu.getScene().getWindow();
            stage.close();
        }
    }

}
