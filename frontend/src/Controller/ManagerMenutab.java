package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import model.Menu;
import Controller.ManagerMenu;
import java.io.IOException;
import java.util.List;

public class ManagerMenutab {

    @FXML
    private GridPane grid;

    @FXML
    private ScrollPane scrollMenu;

    private String type = "tabAll";

//    public void setType(String type) {
//        this.type = type;
//        loadData();
//    }

//    private void loadData() {
//        grid.getChildren().clear();
//        //List<Menu> danhSach = ManagerMenu.getInstance().getListMenu();
//
//        List<Menu> filtered = danhSach.stream()
//                .filter(m -> type.equals("tabAll") || m.getType().equalsIgnoreCase(type))
//                .toList();
//
//        int col = 2, row = 0;
//        for (Menu mon : filtered) {
//            try {
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/item_menu.fxml"));
//                Node node = loader.load();
//
//                ItemMenu itemCtrl = loader.getController();
//                itemCtrl.setData(mon);
//
//                grid.add(node, col, row);
//                col++;
//                if (col == 5) {
//                    col = 2;
//                    row++;
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }


}
