package Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.beans.value.ChangeListener;

import java.io.IOException;
import java.util.*;

public class ManagerMenu {

    @FXML
    private AnchorPane anchorpane;

    @FXML
    private GridPane grid;

    @FXML
    private ScrollPane scroll;

    @FXML
    private ImageView exitButtonMenu;

    @FXML
    private TabPane menuTabPane;

    @FXML
    private Button btnPhongBan;

    @FXML
    private Button btnThucDon;

    @FXML
    private HBox headerContainer;

    private TableListController tableListController;
    private Node tableListView;

    private Map<Tab, TableListController> tabControllers = new HashMap<>();
    private Map<Tab, Node> tabViews = new HashMap<>();

    private Map<Tab, MenuListController> menuTabControllers = new HashMap<>();
    private Map<Tab, Node> menuTabViews = new HashMap<>();

    private ChangeListener<Tab> phongBanTabChangeListener;


    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            Stage stage = (Stage) anchorpane.getScene().getWindow();
            stage.setMaximized(true);
        });

        setupEventHandlers();
        switchToPhongBan();
    }

    private void setupEventHandlers() {
        btnPhongBan.setOnAction(this::onPhongBanClick);
        btnThucDon.setOnAction(this::onThucDonClick);
    }

    @FXML
    public void onExitButtonClick() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận thoát");
        alert.setHeaderText("Bạn có chắc chắn muốn thoát?");
        alert.setContentText("Nhấn OK để thoát, Cancel để hủy.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) exitButtonMenu.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    public void onPhongBanClick(ActionEvent event) {
        switchToPhongBan();
    }

    @FXML
    public void onThucDonClick(ActionEvent event) {
        switchToThucDon();
    }

    public void switchToThucDon() {
        loadHeader("views/ThucDonHeader.fxml", true);
        updateButtonStyles(true);

        // Remove phongBanTabChangeListener nếu còn
        if (phongBanTabChangeListener != null) {
            menuTabPane.getSelectionModel().selectedItemProperty().removeListener(phongBanTabChangeListener);
            phongBanTabChangeListener = null;
        }

        // Xóa các tab cũ đi
        menuTabPane.getTabs().clear();
        menuTabControllers.clear();
        menuTabViews.clear();

        // Tạo các tab thực đơn
        Tab tabAll = new Tab("Tất cả");
        Tab tabFood = new Tab("Món ăn");
        Tab tabDrink = new Tab("Nước uống");
        Tab tabOther = new Tab("Khác");

        try {
            menuTabViews.put(tabAll, loadMenuListView(tabAll, "all"));
            menuTabViews.put(tabFood, loadMenuListView(tabFood, "food"));
            menuTabViews.put(tabDrink, loadMenuListView(tabDrink, "drink"));
            menuTabViews.put(tabOther, loadMenuListView(tabOther, "other"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        tabAll.setContent(menuTabViews.get(tabAll));
        tabFood.setContent(menuTabViews.get(tabFood));
        tabDrink.setContent(menuTabViews.get(tabDrink));
        tabOther.setContent(menuTabViews.get(tabOther));

        menuTabPane.getTabs().addAll(tabAll, tabFood, tabDrink, tabOther);
        menuTabPane.getSelectionModel().select(tabAll);

        menuTabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab != null && menuTabControllers.containsKey(newTab)) {
                MenuListController controller = menuTabControllers.get(newTab);
                controller.refreshMenuItems();
            }
        });
    }


    public void switchToPhongBan() {
        loadHeader("views/PhongBanHeader.fxml", false);
        updateButtonStyles(false);

        menuTabPane.getTabs().clear();
        tabControllers.clear();
        tabViews.clear();

        Tab tabAll = new Tab("Tất cả");
        Tab tabActive = new Tab("Đang hoạt động");
        Tab tabEmpty = new Tab("Đang trống");
        Tab tabBooked = new Tab("Đã đặt lịch");

        try {
            tabViews.put(tabAll, loadTableListView(tabAll, "all"));
            tabViews.put(tabActive, loadTableListView(tabActive, "active"));
            tabViews.put(tabEmpty, loadTableListView(tabEmpty, "empty"));
            tabViews.put(tabBooked, loadTableListView(tabBooked, "booked"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        tabAll.setContent(tabViews.get(tabAll));
        tabActive.setContent(tabViews.get(tabActive));
        tabEmpty.setContent(tabViews.get(tabEmpty));
        tabBooked.setContent(tabViews.get(tabBooked));

        menuTabPane.getTabs().addAll(tabAll, tabActive, tabEmpty, tabBooked);
        menuTabPane.getSelectionModel().select(tabAll);

        if (phongBanTabChangeListener != null) {
            menuTabPane.getSelectionModel().selectedItemProperty().removeListener(phongBanTabChangeListener);
        }

        phongBanTabChangeListener = (obs, oldTab, newTab) -> {
            if (newTab != null && tabControllers.containsKey(newTab)) {
                TableListController controller = tabControllers.get(newTab);
                controller.refreshTables();
            }
        };

        menuTabPane.getSelectionModel().selectedItemProperty().addListener(phongBanTabChangeListener);
    }

    private Node loadTableListView(Tab tab, String filter) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/table_list.fxml"));
        Node view = loader.load();
        TableListController controller = loader.getController();
        controller.setFilter(filter);
        controller.refreshTables();
        tabControllers.put(tab, controller);
        return view;
    }

    private Node loadMenuListView(Tab tab, String filter) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/menu_list.fxml")); // giả sử view này bạn đã có
        Node view = loader.load();
        MenuListController controller = loader.getController();
        controller.setFilter(filter);
        controller.refreshMenuItems();
        menuTabControllers.put(tab, controller);
        return view;
    }


    private void updateButtonStyles(boolean isThucDon) {
        btnThucDon.getStyleClass().removeAll("button_menu", "tab-right");
        btnPhongBan.getStyleClass().removeAll("button_menu", "tab-left");

        if (isThucDon) {
            btnThucDon.getStyleClass().addAll("button_menu", "tab-right");
            btnPhongBan.getStyleClass().add("tab-left");
        } else {
            btnPhongBan.getStyleClass().addAll("button_menu", "tab-left");
            btnThucDon.getStyleClass().add("tab-right");
        }
    }

    public void loadHeader(String fxmlPath, boolean isThucDon) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + fxmlPath));
            Node header = loader.load();
            headerContainer.getChildren().clear();
            headerContainer.getChildren().add(header);

            if (isThucDon) {
                ThucDonHeader headerController = loader.getController();
                if (headerController != null) {
                    headerController.setMainController(this);
                }
            } else {
                PhongBanHeader headerController = loader.getController();
                if (headerController != null) {
                    headerController.setMainController(this);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshTableList() {
        if (tableListController != null) {
            tableListController.refreshTables();
        }
    }

    public void refreshMenuList() {
        Tab selectedTab = menuTabPane.getSelectionModel().getSelectedItem();
        if (selectedTab != null && menuTabControllers.containsKey(selectedTab)) {
            menuTabControllers.get(selectedTab).refreshMenuItems();
        }
    }

}