package Controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Optional;

public class ManagerDashboardController {

    @FXML
    private ImageView logoAvatar;  // Ảnh đại diện của người dùng

    private ContextMenu contextMenu;  // Menu dropdown

    @FXML
    private AnchorPane rootPane; // Gốc của giao diện

    @FXML
    private AnchorPane sidebarContainer; // Chứa sidebar

    @FXML
    private VBox smallSidebar; // Sidebar thu nhỏ

    @FXML
    private VBox expandedSidebar; // Sidebar mở rộng

    @FXML
    private ImageView exitButton; // Nút thoát

    private boolean isExpanded = false; // Trạng thái sidebar

    @FXML
    private void onMouseEnterSidebar() {
        loadSidebar("item_expandSideBar.fxml", 200); // 200px là width của sidebar mở rộng
    }

    @FXML
    private void onMouseExitSidebar() {
        loadSidebar("item_smallSideBar.fxml", 60); // 60px là width của sidebar thu nhỏ
    }
    @FXML
    public void initialize() {
        // Đợi giao diện load xong trước khi thiết lập
        Platform.runLater(() -> {
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setMaximized(true); // Mở rộng cửa sổ

            // Load sidebar nhỏ mặc định
            loadSidebar("/views/item_smallSideBar.fxml", 60);


            // Gán sự kiện hover
            sidebarContainer.setOnMouseEntered(event -> expandSidebar());
            sidebarContainer.setOnMouseExited(event -> collapseSidebar());

            // Khởi tạo menu
            contextMenu = new ContextMenu();

            // Thêm các mục vào menu
            MenuItem information = new MenuItem("Hồ sơ");
            MenuItem logout = new MenuItem("Đăng xuất");


            // Xử lý sự kiện khi chọn menu
            information.setOnAction(event -> System.out.println("Hồ sơ được chọn"));
            logout.setOnAction(event -> logoutAndShowLogin());


            // Thêm các mục vào menu
            contextMenu.getItems().addAll(information, logout);
            contextMenu.getStyleClass().add("context-menu");



        });
    }

    /**
     * Hàm mở rộng sidebar với hiệu ứng
     */
    private void expandSidebar() {
        if (isExpanded) return;
        isExpanded = true;

        TranslateTransition transition = new TranslateTransition(Duration.millis(200), sidebarContainer);
        transition.setToX(0);
        transition.play();

        // Load giao diện sidebar mở rộng
        loadSidebar("/views/item_expandSideBar.fxml", 200);
    }

    private void collapseSidebar() {
        if (!isExpanded) return;
        isExpanded = false;

        TranslateTransition transition = new TranslateTransition(Duration.millis(200), sidebarContainer);
        transition.setToX(0);
        transition.play();

        // Load giao diện sidebar nhỏ
        loadSidebar("/views/item_smallSideBar.fxml", 60);
    }


    /**
     * Hàm hỗ trợ load sidebar từ file FXML
     * @param fxmlFile Đường dẫn file FXML
     */
    private void loadSidebar(String fxmlFile, double newWidth) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            VBox sidebar = loader.load(); // Load giao diện từ file FXML

            // Hiệu ứng thay đổi kích thước
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.millis(200),
                            new KeyValue(sidebarContainer.prefWidthProperty(), newWidth))
            );
            timeline.play();

            sidebarContainer.getChildren().clear(); // Xóa nội dung cũ
            sidebarContainer.getChildren().add(sidebar); // Thêm sidebar mới
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onLogoClick(MouseEvent event) {
        // Lấy vị trí trên màn hình của logo
        double logoX = logoAvatar.localToScreen(0, 0).getX();
        double logoY = logoAvatar.localToScreen(0, 0).getY();

        // Lấy chiều rộng của menu (giả sử khoảng 120px, có thể thay đổi nếu cần)
        double menuWidth = 80;

        // Hiển thị menu ở bên trái logo
        contextMenu.show(logoAvatar, logoX - menuWidth, logoY);
    }
    private void logoutAndShowLogin() {
        try {
            // Đóng cửa sổ hiện tại
            Stage currentStage = (Stage) rootPane.getScene().getWindow();
            currentStage.close();

            // Tải giao diện login.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
            Parent root = loader.load();

            // Tạo cửa sổ mới cho giao diện đăng nhập
            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(root));

            // Ẩn tiêu đề và viền cửa sổ
            loginStage.initStyle(StageStyle.UNDECORATED);

            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Xử lý sự kiện thoát ứng dụng
     */
    @FXML
    private void onExitButtonClick() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận thoát");
        alert.setHeaderText("Bạn có chắc chắn muốn thoát?");
        alert.setContentText("Nhấn OK để thoát, Cancel để hủy.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.close(); // Đóng cửa sổ
        }
    }
}