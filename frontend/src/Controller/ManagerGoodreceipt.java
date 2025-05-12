package Controller;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;

public class ManagerGoodreceipt {

    @FXML
    private StackPane contentArea;

    @FXML
    private HBox boxThemSanPham;

    @FXML
    private HBox boxXoaSanPham;

    @FXML
    private TableView<?> tableStoreHouse;

    @FXML
    public void initialize() {
        autoResizeTable();
    }

    public void showOverview() {
        try {
            Parent fxml = FXMLLoader.load(getClass().getResource("../views/manager_goodreceipt.fxml"));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(fxml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void autoResizeTable() {
        tableStoreHouse.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        int columnCount = tableStoreHouse.getColumns().size();
        for (TableColumn<?, ?> column : tableStoreHouse.getColumns()) {
            column.prefWidthProperty().bind(tableStoreHouse.widthProperty().divide(columnCount));
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

    }

    @FXML
    private void handleXoaSanPham() {

    }
}
