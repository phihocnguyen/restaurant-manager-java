package Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ManagerMenu {

//    private static ManagerMenu instance;
//
//    public ManagerMenu() {
//        instance = this; // gán khi controller được khởi tạo
//    }
//    public static ManagerMenu getInstance() {
//        return instance;
//    }

    //private List<Menu> danhSachMenu = new ArrayList<>();

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

    private Tab tabMenu = new Tab("Thực đơn");
    private Tab tabTable = new Tab("Phòng bàn");

    private ManagerMenutab thucDonController;
    private ManagerTabletab phongBanController;



    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            Stage stage = (Stage) anchorpane.getScene().getWindow();
            stage.setMaximized(true);
        });
//        danhSachMenu.add(new Menu("Cơm gà", 45000, "FOOD", "/images/comga.png"));
//        danhSachMenu.add(new Menu("Phở bò", 50000, "FOOD", "/images/phobo.png"));
//        danhSachMenu.add(new Menu("Nước cam", 20000, "DRINK", "/images/nuoccam.png"));
//        danhSachMenu.add(new Menu("Trà sữa", 35000, "DRINK", "/images/trasua.png"));
//        danhSachMenu.add(new Menu("Khăn lạnh", 5000, "OTHER", "/images/khanlanh.png"));
//        switchToThucDon("tabAll");
//        try {
//            FXMLLoader loaderTD = new FXMLLoader(getClass().getResource("/views/manager_menutab.fxml"));
//            Node thucDonNode = loaderTD.load();
//            thucDonController = loaderTD.getController();
//            tabMenu.setContent(thucDonNode);
//
//            FXMLLoader loaderPB = new FXMLLoader(getClass().getResource("/views/manager_tabletab.fxml"));
//            Node phongBanNode = loaderPB.load();
//            phongBanController = loaderPB.getController();
//            tabTable.setContent(phongBanNode);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        // Load mặc định header Thực đơn
        switchToThucDon();
        loadHeader("views/ThucDonHeader.fxml", true);
    }

//    public List<Menu> getListMenu() {
//        return danhSachMenu;
//    }

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

    public void switchToThucDon() {
        loadHeader("views/ThucDonHeader.fxml", true);
        menuTabPane.getTabs().clear();
        menuTabPane.getTabs().addAll(
                new Tab("Tất cả"),
                new Tab("Món ăn"),
                new Tab("Nước uống"),
                new Tab("Khác")
        );
//        public void switchToThucDon(String type) {
//            menuTabPane.getTabs().clear();
//            menuTabPane.getTabs().add(tabMenu);
//
//            if (thucDonController != null) {
//                thucDonController.setType(type);  // Gọi đúng hàm setType trong ManagerMenutab
//            }
//        }

    }

    public void switchToPhongBan() {
        loadPhongBanHeader("views/PhongBanHeader.fxml", true);
        menuTabPane.getTabs().clear();
        menuTabPane.getTabs().addAll(
                new Tab("Tất cả"),
                new Tab("Đang hoạt động"),
                new Tab("Đang trống"),
                new Tab("Đã đặt lịch")
        );
//        public void switchToPhongBan(String state) {
//            menuTabPane.getTabs().clear();
//            menuTabPane.getTabs().add(tabTable);
//
//            if (phongBanController != null) {
//                phongBanController.setState(state);  // Gọi đúng hàm setType trong ManagerMenutab
//            }
//        }

    }

    public void loadHeader(String fxmlPath, boolean isThucDon) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + fxmlPath));
            Node header = loader.load();
            headerContainer.getChildren().clear();
            headerContainer.getChildren().add(header);

            if (isThucDon) {
                ThucDonHeader headerController = loader.getController();
                headerController.setMainController(this);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadPhongBanHeader(String fxmlPath, boolean isPhongBan) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + fxmlPath));
            Node header = loader.load();
            headerContainer.getChildren().clear();
            headerContainer.getChildren().add(header);

            if (isPhongBan) {
                PhongBanHeader headerController = loader.getController();
                headerController.setMainController(this);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
