package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class VerifyCodeController {
    @FXML
    private TextField codeField;

    @FXML
    private Button btnVerify;

    @FXML
    public void initialize() {
        // Có thể xài trực tiếp setOnAction hoặc dùng @FXML binding
        btnVerify.setOnAction(event -> handleVerifyCode());
    }

    @FXML
    private void handleVerifyCode() {

    }
}
