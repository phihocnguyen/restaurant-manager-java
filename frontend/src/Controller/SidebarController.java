package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;


import java.io.IOException;

public class SidebarController {
    @FXML private HBox boxOverview;

    @FXML
    public void initialize() {
        // Mặc định chọn Tổng quan lúc đầu
        setSelected(boxOverview);
    }

    private void loadViewToContentArea(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();

            ManagerDashboardController.setContent(view); // Gọi hàm tĩnh set content
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOverviewClick(MouseEvent e) {
        setSelected(boxOverview);
        loadViewToContentArea("/views/manager_overview.fxml");
    }
    private HBox currentSelected = null;

    private void setSelected(HBox selectedBox) {
        if (currentSelected != null) {
            currentSelected.getStyleClass().remove("selected");
        }
        selectedBox.getStyleClass().add("selected");
        currentSelected = selectedBox;
    }


}