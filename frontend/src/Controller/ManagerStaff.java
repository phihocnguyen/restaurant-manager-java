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

public class ManagerStaff {

    @FXML
    private StackPane contentArea;

    @FXML
    private HBox boxThemThongTin;

    @FXML
    private HBox boxSuaThongTin;

    @FXML
    private HBox boxXoaThongTin;


    @FXML
    private TableView<?> staffView;



    @FXML
    public void initialize() {
        autoResizeTable();
    }

    public void showOverview() {
        try {
            Parent fxml = FXMLLoader.load(getClass().getResource("../views/manager_staff.fxml"));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(fxml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void autoResizeTable() {
        staffView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        int columnCount = staffView.getColumns().size();
        for (TableColumn<?, ?> column : staffView.getColumns()) {
            column.prefWidthProperty().bind(staffView.widthProperty().divide(columnCount));
        }
    }

    @FXML
    private void handleThemThongTin() {
// Animation click nhẹ
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), boxThemThongTin);
        scaleTransition.setToX(0.95);
        scaleTransition.setToY(0.95);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setCycleCount(2);
        scaleTransition.play();

        scaleTransition.setOnFinished(event -> {
            boxThemThongTin.getStyleClass().add("selected");
        });

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/staff_add.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.centerOnScreen();

            stage.showAndWait(); // block luồng tại đây -> chờ user đóng

            // Sau khi user đóng AddToMenu -> remove trạng thái selected
            boxThemThongTin.getStyleClass().remove("selected"); // <-- thêm dòng này
            boxThemThongTin.setScaleX(1.0);
            boxThemThongTin.setScaleY(1.0);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @FXML
    private void handleSuaThongTin() {
        // Animation click nhẹ
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), boxSuaThongTin);
        scaleTransition.setToX(0.95);
        scaleTransition.setToY(0.95);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setCycleCount(2);
        scaleTransition.play();

        scaleTransition.setOnFinished(event -> {
            boxSuaThongTin.getStyleClass().add("selected");
        });

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/staff_add.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.centerOnScreen();

            stage.showAndWait(); // block luồng tại đây -> chờ user đóng

            // Sau khi user đóng AddToMenu -> remove trạng thái selected
            boxSuaThongTin.getStyleClass().remove("selected"); // <-- thêm dòng này
            boxSuaThongTin.setScaleX(1.0);
            boxSuaThongTin.setScaleY(1.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleXoaThongTin() {
// Animation click nhẹ
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), boxXoaThongTin);
        scaleTransition.setToX(0.95);
        scaleTransition.setToY(0.95);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setCycleCount(2);
        scaleTransition.play();

        scaleTransition.setOnFinished(event -> {
            boxXoaThongTin.getStyleClass().add("selected");
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
            boxXoaThongTin.getStyleClass().remove("selected"); // <-- thêm dòng này
            boxXoaThongTin.setScaleX(1.0);
            boxXoaThongTin.setScaleY(1.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
