package Controller;


import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import Class.SanPham;


import java.io.IOException;

public class QL_ThucDonController {
    @FXML
    private StackPane contentArea;
    @FXML
    private HBox boxThemSanPham;

    @FXML
    private HBox boxXoaSanPham;

    @FXML
    private TableView<SanPham> tableView;
    @FXML
    private TableColumn<SanPham, String> colMaSP;
    @FXML
    private TableColumn<SanPham, String> colTenSP;
    @FXML
    private TableColumn<SanPham, String> colLoaiSP;
    @FXML
    private TableColumn<SanPham, Double> colGiaVon;
    @FXML
    private TableColumn<SanPham, Double> colGiaBan;
    @FXML
    private TableColumn<SanPham, Integer> colSoLuong;
    @FXML
    private TableColumn<SanPham, Void> colXem;
    @FXML
    private TableColumn<SanPham, Void> colChinhSua;

    public void initialize() {
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colMaSP.setCellValueFactory(new PropertyValueFactory<>("maSP"));
        colTenSP.setCellValueFactory(new PropertyValueFactory<>("tenSP"));
        colLoaiSP.setCellValueFactory(new PropertyValueFactory<>("loaiSP"));
        colGiaVon.setCellValueFactory(new PropertyValueFactory<>("giaVon"));
        colGiaBan.setCellValueFactory(new PropertyValueFactory<>("giaBan"));
        colSoLuong.setCellValueFactory(new PropertyValueFactory<>("soLuongConLai"));

        addButtonXem();
        addButtonChinhSua();

        // Thêm dữ liệu mẫu
        tableView.getItems().addAll(
                new SanPham("MN01", "Khoai tây chiên", "FOOD", 30039, 49000, 0),
                new SanPham("MN02", "Coca cola", "DRINK", 5000, 12000, 0),
               new SanPham("MN03", "Khăn ướt", "OTHER", 5000, 14000, 0),
                new SanPham("MN04", "Mirinda xá xị", "DRINK", 5000, 12000, 0)
        );
    }


    public void showOverview() {
        try {
            Parent fxml = FXMLLoader.load(getClass().getResource("../views/manager_menutab.fxml"));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(fxml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addButtonXem() {
        Callback<TableColumn<SanPham, Void>, TableCell<SanPham, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<SanPham, Void> call(final TableColumn<SanPham, Void> param) {
                final TableCell<SanPham, Void> cell = new TableCell<>() {
                    private final Button btn = new Button();

                    {
                        btn.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/img/eye_icon.png"), 20, 20, true, true)));
                        btn.setStyle("-fx-background-color: transparent;");

                        btn.setOnAction(event -> {
                            SanPham sp = getTableView().getItems().get(getIndex());
                            System.out.println("Xem sản phẩm: " + sp.getTenSP());
                            // Xử lý mở xem chi tiết ở đây
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        colXem.setCellFactory(cellFactory);


    }

    private void addButtonChinhSua() {
        Callback<TableColumn<SanPham, Void>, TableCell<SanPham, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<SanPham, Void> call(final TableColumn<SanPham, Void> param) {
                final TableCell<SanPham, Void> cell = new TableCell<>() {
                    private final Button btn = new Button();

                    {
                        btn.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/img/edit_icon.png"), 20, 20, true, true)));
                        btn.setStyle("-fx-background-color: transparent;");

                        btn.setOnAction(event -> {
                            SanPham sp = getTableView().getItems().get(getIndex());
                            System.out.println("Chỉnh sửa sản phẩm: " + sp.getTenSP());
                            // Xử lý mở form chỉnh sửa ở đây
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };
        colChinhSua.setCellFactory(cellFactory);

    }
    @FXML
    private void handleThemSanPham() {
        // Animation click nhẹ
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), boxThemSanPham);
        scaleTransition.setToX(0.95);
        scaleTransition.setToY(0.95);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setCycleCount(2);
        scaleTransition.play();

        scaleTransition.setOnFinished(event -> {
            boxThemSanPham.getStyleClass().add("selected");
        });

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/add_to_menu.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.centerOnScreen();

            stage.showAndWait(); // block luồng tại đây -> chờ user đóng

            // Sau khi user đóng AddToMenu -> remove trạng thái selected
            boxThemSanPham.getStyleClass().remove("selected"); // <-- thêm dòng này
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleXoaSanPham() {
        // Animation click nhẹ
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), boxXoaSanPham);
        scaleTransition.setToX(0.95);
        scaleTransition.setToY(0.95);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setCycleCount(2);
        scaleTransition.play();

        scaleTransition.setOnFinished(event -> {
            boxXoaSanPham.getStyleClass().add("selected");
        });

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/confirm_deleted.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.centerOnScreen();

            stage.showAndWait(); // block luồng tại đây -> chờ user đóng

            // Sau khi user đóng AddToMenu -> remove trạng thái selected
            boxXoaSanPham.getStyleClass().remove("selected"); // <-- thêm dòng này
        } catch (IOException e) {
            e.printStackTrace();
        }

    }





}


