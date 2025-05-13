package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ReceiptAdd {

    @FXML
    private TextField txtMaNK;

    @FXML
    private DatePicker dpkNgayNK;

    @FXML
    private HBox btnHuyBo;

    @FXML
    private HBox btnXacNhan;

    // Controller cha để reset trạng thái
    private ManagerGoodreceipt parentController;
    private HBox buttonThemThongTin;

    // Hàm gọi từ ManagerGoodreceipt để truyền tham chiếu
    public void init(ManagerGoodreceipt controller, HBox btnThemThongTin) {
        this.parentController = controller;
        this.buttonThemThongTin = btnThemThongTin;
    }

    @FXML
    private void initialize() {
        // Gán sự kiện click cho HBox
        btnHuyBo.setOnMouseClicked(this::handleHuyBo);
        btnXacNhan.setOnMouseClicked(this::handleXacNhan);
    }

    private void handleHuyBo(MouseEvent event) {
        // Tìm AnchorPane cha (creatPhieuNhap)
        AnchorPane currentPane = (AnchorPane) ((Node) event.getSource()).getScene().lookup("#creatPhieuNhap");
        if (currentPane != null) {
            currentPane.getChildren().clear();
            currentPane.setPrefHeight(0);
        }

        // Bỏ class "selected" của nút Thêm thông tin (nếu được truyền)
        if (buttonThemThongTin != null) {
            buttonThemThongTin.getStyleClass().remove("selected");
            buttonThemThongTin.setScaleX(1.0);
            buttonThemThongTin.setScaleY(1.0);
            buttonThemThongTin.setStyle(""); // Xóa inline style nếu có
        }

    }

    private void handleXacNhan(MouseEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/receipt_detail.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Chi tiết phiếu nhập");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // nếu muốn block cửa sổ cha
            stage.initStyle(StageStyle.UNDECORATED); // hoặc UNDECORATED, UTILITY...

            stage.show();

        } catch (IOException e) {
            System.err.println("Không thể mở giao diện chi tiết phiếu nhập: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
