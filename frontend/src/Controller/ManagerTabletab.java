package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.GridPane;
import model.Menu;
import model.Table;

import java.io.Console;
import java.io.IOException;
import java.security.cert.TrustAnchor;
import java.util.List;


public class ManagerTabletab {

    @FXML
    private GridPane grid;

    @FXML
    private ScrollPane scrollTable;


//    private String state = "tabAll";
//
//    public void setState(String state) {
//        this.state = state;
//        loadData();
//    }
//
//    private void loadData() {
//        grid.getChildren().clear();
//
//        List<Table> danhSach = ManagerMenu.getInstance().getListTable();
//
//        List<Table> filtered = danhSach.stream()
//                .filter(b -> state.equals("ALL") || b.getState().equalsIgnoreCase(state))
//                .toList();
//
//        int col = 1, row = 0;
//        for (Table ban : filtered) {
//            try {
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/item_table.fxml"));
//                Node node = loader.load();
//
//                ItemTable itemCtrl = loader.getController();
//                itemCtrl.setData(ban);
//
//                grid.add(node, col, row);
//                col++;
//                if (col == 5) {
//                    col = 1;
//                    row++;
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }


}
