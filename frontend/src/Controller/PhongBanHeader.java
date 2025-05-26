package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class PhongBanHeader {

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    private Button addTableButton;

    @FXML
    private Button refreshButton;

    private ManagerMenu managerMenu;

    @FXML
    private Button btnPhongBan;

    @FXML
    private Button btnThucDon;


    public void setMainController(ManagerMenu mainController) {
        this.managerMenu = mainController;

        btnThucDon.setOnAction(e -> {
            if (this.managerMenu != null) {
                managerMenu.switchToThucDon();
            } else {
                System.err.println("ManagerMenu is NULL!");
            }
        });
    }

    @FXML
    public void initialize() {
        setupEventHandlers();
    }

    private void setupEventHandlers() {
        if (searchButton != null) {
            searchButton.setOnAction(e -> onSearchClick());
        }

        if (addTableButton != null) {
            addTableButton.setOnAction(e -> onAddTableClick());
        }

        if (refreshButton != null) {
            refreshButton.setOnAction(e -> onRefreshClick());
        }

        if (searchField != null) {
            searchField.setOnAction(e -> onSearchClick());
        }
    }

    @FXML
    private void onSearchClick() {
        String searchText = searchField.getText().trim();
        if (!searchText.isEmpty()) {
            // Implement search functionality
            System.out.println("Searching for: " + searchText);
            // TODO: Implement search logic
        }
    }

    @FXML
    private void onAddTableClick() {
        // TODO: Implement add table functionality
        System.out.println("Add new table clicked");
    }

    @FXML
    private void onRefreshClick() {
        if (managerMenu != null) {
            managerMenu.refreshTableList();
        }
    }
}