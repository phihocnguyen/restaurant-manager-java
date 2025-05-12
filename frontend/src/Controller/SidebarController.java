package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;


import java.io.IOException;

public class SidebarController {
    @FXML private HBox boxOverview;
    @FXML private HBox boxQL_ThucDon;
    @FXML private HBox boxIngredient;
    @FXML private HBox boxDoanhThu;
    @FXML private HBox boxStoreHouse;


    private HBox currentSelected = null;

    @FXML
    public void initialize() {
        if (SidebarState.selectedTabId != null) {
            HBox selectedBox = findBoxById(SidebarState.selectedTabId);
            setSelected(selectedBox);

            // Load FXML tương ứng
            switch (SidebarState.selectedTabId) {
                case "boxOverview":
                    loadViewToContentArea("/views/manager_overview.fxml");
                    break;
                case "boxQL_ThucDon":
                    loadViewToContentArea("/views/manager_menutab.fxml");
                    break;
                case "boxIngredient":
                    loadViewToContentArea("/views/manager_ingredient.fxml");
                    break;
                case "boxDoanhThu":
                    loadViewToContentArea("/views/manager_doanhthu.fxml");
                    break;
                case "boxStoreHouse":
                    loadViewToContentArea("/views/manager_goodreceipt.fxml");
                    break;
                // Thêm case khác nếu bạn có thêm tab
            }
        } else {
            setSelected(boxOverview);
            loadViewToContentArea("/views/manager_overview.fxml");
        }
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
    private void setSelected(HBox selectedBox) {
        if (currentSelected != null) {
            currentSelected.getStyleClass().remove("selected");
        }
        selectedBox.getStyleClass().add("selected");
        currentSelected = selectedBox;

        SidebarState.selectedTabId = selectedBox.getId();
    }
    private HBox findBoxById(String id) {
        switch (id) {
            case "boxOverview":
                return boxOverview;
            case "boxQL_ThucDon":
                return boxQL_ThucDon;
            case "boxIngredient":
                return boxIngredient;
            case "boxDoanhThu":
                return boxDoanhThu;
            case "boxStoreHouse":
                return boxStoreHouse;
            // Thêm case cho những nút khác nếu có
            default:
                return boxOverview;
        }
    }

    @FXML
    private void handleOverviewClick(MouseEvent e) {
        setSelected(boxOverview);
        loadViewToContentArea("/views/manager_overview.fxml");
    }

    @FXML
    private void handleQL_ThucDonClick(MouseEvent e) {
        setSelected(boxQL_ThucDon);
        loadViewToContentArea("/views/manager_menutab.fxml");
    }

    @FXML
    private void handleIngredientClick(MouseEvent e) {
        setSelected(boxIngredient);
        loadViewToContentArea("/views/manager_ingredient.fxml");
    }

    @FXML
    private void handleDoanhThuClick(MouseEvent e) {
        setSelected(boxDoanhThu);
        loadViewToContentArea("/views/manager_doanhthu.fxml");
    }

    @FXML
    private void handleStoreHouseClick(MouseEvent e) {
        setSelected(boxStoreHouse);
        loadViewToContentArea("/views/manager_goodreceipt.fxml");
    }






}