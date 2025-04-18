package Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ThucDonHeader {
    private ManagerMenu managerMenu;

    @FXML
    private Button btnPhongBan;

    @FXML
    private Button btnThucDon;

    public void setMainController(ManagerMenu controller) {
        this.managerMenu = controller;
    }

    @FXML
    public void initialize() {
        btnThucDon.setOnAction(e -> {
            managerMenu.switchToThucDon();
        });

        btnPhongBan.setOnAction(e -> {
            managerMenu.switchToPhongBan();
        });
    }
}
