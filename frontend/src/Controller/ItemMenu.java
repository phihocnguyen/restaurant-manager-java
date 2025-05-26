package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.MenuItem;

import java.io.File;
import java.io.IOException;

public class ItemMenu {

    @FXML
    private ImageView itemImage;

    @FXML
    private Label itemNameLabel;

    @FXML
    private Label itemPriceLabel;

    public void setData(MenuItem item) {
        itemNameLabel.setText(item.getItemName());
        itemPriceLabel.setText(String.format("%.0f VNĐ", item.getItemSprice()));

        String imagePath = item.getItemImg(); // ví dụ: "bunBoHue.png"

        try {
            // Thử load ảnh từ thư mục resources/imgfood
            java.net.URL imageURL = getClass().getResource("/imgfood/" + imagePath);

            if (imageURL != null) {
                itemImage.setImage(new Image(imageURL.toExternalForm(), true));
            } else {
                throw new IOException("Ảnh không tồn tại trong resources");
            }

        } catch (Exception e) {
            try {
                // Nếu lỗi, dùng ảnh mặc định nội bộ
                itemImage.setImage(new Image(getClass().getResourceAsStream("/imgfood/default.png")));
            } catch (Exception ex) {
                // Nếu cả ảnh mặc định cũng không có, dùng ảnh mặc định từ Internet
                String fallbackUrl = "https://media.istockphoto.com/id/1457433817/photo/group-of-healthy-food-for-flexitarian-diet.jpg?s=612x612&w=0&k=20&c=v48RE0ZNWpMZOlSp13KdF1yFDmidorO2pZTu2Idmd3M=";
                itemImage.setImage(new Image(fallbackUrl, true));
            }
        }
    }


}
