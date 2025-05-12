package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;


public class LoginController {
    public Button btnForgetPassword;
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

    @FXML
    private void handleForgetPassword() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/forget_password.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initStyle(javafx.stage.StageStyle.UNDECORATED); // Ẩn title bar
            stage.setResizable(false);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Không thể mở giao diện quên mật khẩu.");
            alert.showAndWait();
        }
    }
    @FXML
    private void handleOpenManagerDashboard() {
        openNewStage("/views/manager_dashboard.fxml");
    }

    @FXML
    private void handleOpenManagerMenu() {
        openNewStage("/views/manager_menu.fxml");
    }

    private void openNewStage(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initStyle(javafx.stage.StageStyle.UNDECORATED); // Không có thanh tiêu đề
            stage.setResizable(false);
            stage.show();

            // Optional: tắt login nếu muốn
            ((Stage) closeIcon.getScene().getWindow()).close();

        } catch (IOException e) {
            e.printStackTrace();
            showError("Không thể mở giao diện: " + fxmlPath);
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
