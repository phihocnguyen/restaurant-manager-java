package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class ManagerIngredientController {
    @FXML
    private StackPane contentArea;

    public void showOverview() {
        try {
            Parent fxml = FXMLLoader.load(getClass().getResource("../views/manager_ingredient.fxml"));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(fxml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


