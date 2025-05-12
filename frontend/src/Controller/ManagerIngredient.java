package Controller;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;

public class ManagerIngredient {

    @FXML
    private StackPane contentArea;

    @FXML
    private HBox boxThemSanPham;

    @FXML
    private HBox boxSuaSanPham;

    @FXML
    private HBox boxXoaSanPham;

    @FXML
    private TableView<?> tableIngredient;



    @FXML
    public void initialize() {
        autoResizeTable();
    }

    public void showOverview() {
        try {
            Parent fxml = FXMLLoader.load(getClass().getResource("../views/manager_ingredient.fxml"));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(fxml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void autoResizeTable() {
        tableIngredient.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        int columnCount = tableIngredient.getColumns().size();
        for (TableColumn<?, ?> column : tableIngredient.getColumns()) {
            column.prefWidthProperty().bind(tableIngredient.widthProperty().divide(columnCount));
        }
    }


    @FXML
    private void handleThemSanPham() {
        // Animation click nhẹ
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), boxThemSanPham);
        scaleTransition.setToX(0.95);
        scaleTransition.setToY(0.95);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setCycleCount(2);
        scaleTransition.play();

        scaleTransition.setOnFinished(event -> {
            boxThemSanPham.getStyleClass().add("selected");
        });

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ingredient_add.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.centerOnScreen();

            stage.showAndWait(); // block luồng tại đây -> chờ user đóng

            // Sau khi user đóng AddToMenu -> remove trạng thái selected
            boxThemSanPham.getStyleClass().remove("selected"); // <-- thêm dòng này
            boxThemSanPham.setScaleX(1.0);
            boxThemSanPham.setScaleY(1.0);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void handleSuaSanPham() {
        // Animation click nhẹ
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), boxSuaSanPham);
        scaleTransition.setToX(0.95);
        scaleTransition.setToY(0.95);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setCycleCount(2);
        scaleTransition.play();

        scaleTransition.setOnFinished(event -> {
            boxSuaSanPham.getStyleClass().add("selected");
        });

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ingredient_add.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.centerOnScreen();

            stage.showAndWait(); // block luồng tại đây -> chờ user đóng

            // Sau khi user đóng AddToMenu -> remove trạng thái selected
            boxSuaSanPham.getStyleClass().remove("selected"); // <-- thêm dòng này
            boxSuaSanPham.setScaleX(1.0);
            boxSuaSanPham.setScaleY(1.0);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void handleXoaSanPham() {
        // Animation click nhẹ
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), boxXoaSanPham);
        scaleTransition.setToX(0.95);
        scaleTransition.setToY(0.95);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setCycleCount(2);
        scaleTransition.play();

        scaleTransition.setOnFinished(event -> {
            boxXoaSanPham.getStyleClass().add("selected");
        });

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/confirm_deleted.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.centerOnScreen();

            stage.showAndWait(); // block luồng tại đây -> chờ user đóng

            // Sau khi user đóng AddToMenu -> remove trạng thái selected
            boxXoaSanPham.getStyleClass().remove("selected"); // <-- thêm dòng này
            boxXoaSanPham.setScaleX(1.0);
            boxXoaSanPham.setScaleY(1.0);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
