package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class SendEmailController {

    @FXML
    private TextField emailField;

    private AnchorPane contentArea;

    public void setContentArea(AnchorPane contentArea) {
        this.contentArea = contentArea;
    }

    @FXML
    private void handleSendEmail() {
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            showError("Vui lòng nhập email.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/verify_code.fxml"));
            Parent verifyView = loader.load();

            VerifyCodeController controller = loader.getController();
            controller.setEmail(email); // Truyền email
            controller.setContentArea(contentArea);

            contentArea.getChildren().setAll(verifyView);
            AnchorPane.setTopAnchor(verifyView, 0.0);
            AnchorPane.setBottomAnchor(verifyView, 0.0);
            AnchorPane.setLeftAnchor(verifyView, 0.0);
            AnchorPane.setRightAnchor(verifyView, 0.0);

        } catch (IOException e) {
            e.printStackTrace();
            showError("Không thể tải giao diện xác nhận mã.");
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
