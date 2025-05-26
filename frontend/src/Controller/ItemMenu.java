package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import model.Menu;

public class ItemMenu {

    @FXML
    private ImageView img;

    @FXML
    private Label name;

    @FXML
    private Label price;

    public void setData(Menu mon) {
        name.setText(mon.getName());
    }

}
