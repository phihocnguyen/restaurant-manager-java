package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import model.Table;

public class ItemTable {

    @FXML
    private HBox tableItemContainer;

    @FXML
    private ImageView img;

    @FXML
    private Label number;

    @FXML
    private Label statusLabel;

    public void setTableData(Table table) {
        number.setText("Bàn " + table.getTabNum());

        if (table.isDeleted()) {
            statusLabel.setText("Đã xóa");
            tableItemContainer.setStyle("-fx-background-color: #cccccc;");
            img.setVisible(false); // Ẩn ảnh nếu bàn đã xóa
        } else if (table.isBooked()) {
            statusLabel.setText("Đã đặt lịch");
            tableItemContainer.setStyle("-fx-background-color: #fff3cd;");
            img.setVisible(true);
        } else if (table.isActive()) {
            statusLabel.setText("Đang hoạt động");
            tableItemContainer.setStyle("-fx-background-color: #f8d7da;");
            img.setVisible(true);
        } else {
            statusLabel.setText("Đang trống");
            tableItemContainer.setStyle("-fx-background-color: #d4edda;");
            img.setVisible(true);
        }
    }
}