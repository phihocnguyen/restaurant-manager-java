package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.Optional;


public class LoginController {
    @FXML
    private ImageView closeIcon; // Hình ảnh 'X'

    @FXML
    private ImageView minimizeIcon; // Hình ảnh '_'

    @FXML
    private void closeApp() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận thoát");
        alert.setHeaderText("Bạn có chắc chắn muốn thoát?");
        alert.setContentText("Nhấn OK để thoát, Cancel để hủy.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Lấy cửa sổ hiện tại và đóng ứng dụng
            Stage stage = (Stage) closeIcon.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    private void minimizeApp() {
        // Lấy cửa sổ hiện tại và thu nhỏ ứng dụng
        Stage stage = (Stage) minimizeIcon.getScene().getWindow();
        stage.setIconified(true);
    }

}
