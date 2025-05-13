package Controller;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ReceiptDetail {


    @FXML
    private ComboBox<String> comboLoaiNhap;

    @FXML
    private DatePicker dpkNgayNK;

    @FXML
    private Button btnThem;

    @FXML
    private Button btnXoa;

    @FXML
    private TableView<?> receiptdetailView;

    @FXML
    private ComboBox<String> cbTenNL;

    @FXML
    private TextField txtSLNhap;

    @FXML
    private TextField txtDonGia;
    @FXML
    private ImageView exitButton;

    @FXML
    public void initialize() {
        autoResizeTable();
    }



    private void autoResizeTable() {
        receiptdetailView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        int columnCount = receiptdetailView.getColumns().size();
        for (TableColumn<?, ?> column : receiptdetailView.getColumns()) {
            column.prefWidthProperty().bind(receiptdetailView.widthProperty().divide(columnCount));
        }
    }

    @FXML
    private void onThemButtonClick() {

    }

    @FXML
    private void onXoaButtonClick() {

    }

    @FXML
    private void onExitButtonClick() {

        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();

    }
}
