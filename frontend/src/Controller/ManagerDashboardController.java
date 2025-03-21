package Controller;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.w3c.dom.css.CSS2Properties;

import java.io.IOException;
import java.util.Optional;

public class ManagerDashboardController {

    public ImageView exitButton;
    public VBox sidebarContainer;
    public BorderPane borderPane;
    @FXML
    private AnchorPane rootPane;

    @FXML
    private VBox sideMenu; // Danh mục

    @FXML
    private ImageView menuButton;


    private boolean isMenuOpen = false; // Trạng thái menu

    @FXML
    private VBox smallSidebar;  // Sidebar nhỏ


    @FXML
    private VBox expandedSidebar;
    private boolean isExpanded = false; // Trạng thái sidebar

    @FXML
    public void initialize() {
        // Đợi cho đến khi Scene được gán trước khi lấy Stage
        Platform.runLater(() -> {
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setMaximized(true); // Không fullscreen, chỉ mở rộng cửa sổ

            smallSidebar.setVisible(true);
            expandedSidebar.setVisible(false);

            menuButton.setOnMouseClicked(event -> onMenuButtonClick());


        });

    }
    @FXML
    private void toggleSidebar() {
        if (sideMenu == null) return; // Đảm bảo sideMenu không null

        TranslateTransition slide = new TranslateTransition(Duration.millis(300), sideMenu);
        slide.stop(); // Dừng animation cũ nếu có

        if (isMenuOpen) {
            slide.setToX(-200); // Ẩn menu
        } else {
            slide.setToX(0); // Hiện menu
        }

        slide.setOnFinished(e -> isMenuOpen = !isMenuOpen); // Đảo trạng thái menu sau animation
        slide.play();
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
            stage.close(); // Thoát ứng dụng
        }
    }
    @FXML
    private void onMenuButtonClick() {
        try {
            Pane newSidebar;
            if (isExpanded) {
                newSidebar = FXMLLoader.load(getClass().getResource("/views/item_smallSideBar.fxml"));
            } else {
                newSidebar = FXMLLoader.load(getClass().getResource("/views/item_expandSideBar.fxml"));
            }


            borderPane.setLeft(newSidebar); // Cập nhật sidebar
            isExpanded = !isExpanded; // Đảo trạng thái
        } catch (IOException e) {
            e.printStackTrace();
        }
    }






}
