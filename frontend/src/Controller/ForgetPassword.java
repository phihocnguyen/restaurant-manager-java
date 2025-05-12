package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ForgetPassword {

    @FXML
    private AnchorPane contentArea;

    @FXML
    private ImageView exitButton;

    @FXML
    public void initialize() {
        loadSendEmailView();
    }

    @FXML
    private void onExitButtonClick(MouseEvent event) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    private void loadSendEmailView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/send_email.fxml"));
            Parent view = loader.load();

            SendEmailController controller = loader.getController();
            controller.setContentArea(contentArea);

            contentArea.getChildren().setAll(view);
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
