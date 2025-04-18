package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import model.Table;

public class ItemTable {

    @FXML
    private ImageView img;

    @FXML
    private Label number;

    private String state;

    public void setData(Table ban) {
        number.setText(ban.getName()); // hiển thị tên bàn
        this.state = ban.getState();   // lưu trạng thái bàn (ví dụ: "Trống", "Đang hoạt động", ...)

        // Có thể xử lý ảnh theo trạng thái nếu cần
        // Ví dụ: đổi màu hoặc icon tương ứng
    }
}
