package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class AddToMenuController {
    @FXML
    private ComboBox<String> cbLoaiMon;

    @FXML
    private Button btnLuuMon;

    @FXML
    private AnchorPane contentArea;

    @FXML
    private ImageView exitButton;

    @FXML
    public void initialize() {
        cbLoaiMon.getItems().addAll("Món ăn", "Đồ uống", "Khác");

        cbLoaiMon.setOnAction(event -> {
            String selected = cbLoaiMon.getSelectionModel().getSelectedItem();
            loadContent(selected);
        });
    }

    private void loadContent(String selected) {
        try {
            FXMLLoader loader;
            Parent view;
            if ("Món ăn".equals(selected)) {
                loader = new FXMLLoader(getClass().getResource("/views/add_food.fxml"));
            } else {
                loader = new FXMLLoader(getClass().getResource("/views/add_other.fxml"));
            }
            view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onExitButtonClick() {

            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.close();

    }

    @FXML
    public void handleLuuMon(MouseEvent event) {

    }


}
