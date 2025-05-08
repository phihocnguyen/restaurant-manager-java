package Controller;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Optional;

public class ManagerDashboardController {

    @FXML
    private ImageView logoAvatar;



    private ContextMenu contextMenu;

    @FXML
    private AnchorPane rootPane;
    @FXML
    private VBox sidebarContainer;

    @FXML
    private StackPane contentArea;
    private static StackPane staticContentArea;


    @FXML
    private ImageView exitButton;

    private boolean isExpanded = false;
    private String currentSidebar = "";

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setMaximized(true);


            loadSidebar("/views/item_smallSideBar.fxml", 60);

            contextMenu = new ContextMenu();

            MenuItem information = new MenuItem("Hồ sơ");
            MenuItem logout = new MenuItem("Đăng xuất");

            information.setOnAction(event -> System.out.println("Hồ sơ được chọn"));
            logout.setOnAction(event -> logoutAndShowLogin());

            contextMenu.getItems().addAll(information, logout);
            information.getStyleClass().add("custom-menu-item");
            logout.getStyleClass().addAll("custom-menu-item", "logout-item");



            sidebarContainer.setOnMouseEntered(e -> expandSidebar());
            sidebarContainer.setOnMouseExited(e -> collapseSidebar());

            staticContentArea = contentArea;
        });
    }

    private void expandSidebar() {
        if (isExpanded) return;
        isExpanded = true;
        loadSidebar("/views/item_expandSideBar.fxml", 200);
    }

    private void collapseSidebar() {
        if (!isExpanded) return;
        isExpanded = false;
        loadSidebar("/views/item_smallSideBar.fxml", 60);
    }

    private void loadSidebar(String fxmlFile, double newWidth) {
        if (currentSidebar.equals(fxmlFile)) return;
        currentSidebar = fxmlFile;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            VBox sidebar = loader.load();

            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.millis(200),
                            new KeyValue(sidebarContainer.prefWidthProperty(), newWidth))
            );
            timeline.play();

            sidebarContainer.getChildren().clear();
            sidebarContainer.getChildren().add(sidebar);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onLogoClick(MouseEvent event) {
        double logoX = logoAvatar.localToScreen(0, 0).getX();
        double logoY = logoAvatar.localToScreen(0, 0).getY();
        double menuWidth = 60;
        contextMenu.show(logoAvatar, logoX + menuWidth, logoY);
    }

    private void logoutAndShowLogin() {
        try {
            Stage currentStage = (Stage) rootPane.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
            Parent root = loader.load();

            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(root));
            loginStage.initStyle(StageStyle.UNDECORATED);
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onExitButtonClick() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận thoát");
        alert.setHeaderText("Bạn có chắc chắn muốn thoát?");
        alert.setContentText("Nhấn OK để thoát, Cancel để hủy.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.close();
        }
    }
    public static void setContent(Node node) {
        if (staticContentArea != null) {
            staticContentArea.getChildren().setAll(node);

            // Force full size
            AnchorPane.setTopAnchor(node, 0.0);
            AnchorPane.setBottomAnchor(node, 0.0);
            AnchorPane.setLeftAnchor(node, 0.0);
            AnchorPane.setRightAnchor(node, 0.0);
        }
    }


}