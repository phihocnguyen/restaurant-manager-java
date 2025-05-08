package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class AddFoodController {

    @FXML
    public Button btnLuuCongThuc;
    @FXML
    public Button btnThemNguyenLieu;
    @FXML
    public Button btnLuuXoaNguyenLieu;

    @FXML
    private VBox contentArea; // Hoặc VBox contentArea cũng được nếu bạn dùng VBox

    @FXML
    public void handleLuuCongThuc(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/add_other.fxml"));
            Parent newContent = loader.load();

            contentArea.getChildren().clear(); // Chỉ clear vùng content thôi
            contentArea.getChildren().add(newContent); // Add giao diện mới vô

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleThemNguyenLieu(MouseEvent event) {

    }

    @FXML
    public void handleXoaNguyenLieu(MouseEvent event) {

    }

}
