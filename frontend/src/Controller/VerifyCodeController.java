package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class VerifyCodeController {

    @FXML
    private TextField codeField;

    @FXML
    private Button btnVerify;

    @FXML
    private Button btnReturnEnterEmail;

    private String email;
    private AnchorPane contentArea;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setContentArea(AnchorPane contentArea) {
        this.contentArea = contentArea;
    }

    @FXML
    public void initialize() {
        btnVerify.setOnAction(event -> handleVerifyCode());
        btnReturnEnterEmail.setOnAction(event -> handleReturnEnterEmail());
    }

    @FXML
    private void handleVerifyCode() {
        String inputCode = codeField.getText().trim();
        if (inputCode.equals("123456")) {
            try {
                Parent accountView = FXMLLoader.load(getClass().getResource("/views/account.fxml"));
                contentArea.getChildren().setAll(accountView);
                AnchorPane.setTopAnchor(accountView, 0.0);
                AnchorPane.setBottomAnchor(accountView, 0.0);
                AnchorPane.setLeftAnchor(accountView, 0.0);
                AnchorPane.setRightAnchor(accountView, 0.0);
            } catch (IOException e) {
                e.printStackTrace();
                showError("Không thể tải giao diện tài khoản.");
            }
        } else {
            showError("Mã xác nhận không đúng.");
        }
    }

    @FXML
    private void handleReturnEnterEmail() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/send_email.fxml"));
            Parent sendEmailView = loader.load();

            SendEmailController controller = loader.getController();
            controller.setContentArea(contentArea);

            contentArea.getChildren().setAll(sendEmailView);
            AnchorPane.setTopAnchor(sendEmailView, 0.0);
            AnchorPane.setBottomAnchor(sendEmailView, 0.0);
            AnchorPane.setLeftAnchor(sendEmailView, 0.0);
            AnchorPane.setRightAnchor(sendEmailView, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
            showError("Không thể quay lại nhập email.");
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
