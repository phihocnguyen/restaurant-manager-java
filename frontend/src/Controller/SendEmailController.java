package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SendEmailController {

    @FXML
    private TextField emailField;

    @FXML
    private Button btnSendEmail;

    @FXML
    public void initialize() {
        // Có thể xài trực tiếp setOnAction hoặc dùng @FXML binding
        btnSendEmail.setOnAction(event -> handleSendEmail());
    }

    @FXML
    private void handleSendEmail() {

    }

}
