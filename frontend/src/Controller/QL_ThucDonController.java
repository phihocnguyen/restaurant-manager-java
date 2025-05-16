package Controller;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QL_ThucDonController {
    @FXML
    private StackPane contentArea;

    @FXML
    private ProgressIndicator spinner;

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

    private final OkHttpClient client = new OkHttpClient();

    public void initialize() {
        setupTableView();
        loadItemsFromAPI();
    }

    private void setupTableView() {
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colMaSP.setCellValueFactory(new PropertyValueFactory<>("maSP"));
        colTenSP.setCellValueFactory(new PropertyValueFactory<>("tenSP"));
        colLoaiSP.setCellValueFactory(new PropertyValueFactory<>("loaiSP"));
        colGiaVon.setCellValueFactory(new PropertyValueFactory<>("giaVon"));
        colGiaBan.setCellValueFactory(new PropertyValueFactory<>("giaBan"));
        colSoLuong.setCellValueFactory(new PropertyValueFactory<>("soLuongConLai"));

        addButtonXem();
        addButtonChinhSua();
    }

    private void loadItemsFromAPI() {
        javafx.application.Platform.runLater(() -> {
            spinner.setVisible(true);
            tableView.setDisable(true);
        });
        new Thread(() -> {
            try {
                Request request = new Request.Builder()
                        .url("http://localhost:8080/items")
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }

                    String responseData = response.body().string();
                    List<SanPham> items = parseApiResponse(responseData);

                    javafx.application.Platform.runLater(() -> {
                        tableView.getItems().setAll(items);
                        spinner.setVisible(false);
                        tableView.setDisable(false);
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Handle error
                javafx.application.Platform.runLater(() -> {
                    // Show error or fallback data
                    tableView.getItems().addAll(
                            new SanPham("ERR", "Failed to load data", "ERROR", 0, 0, 0)
                    );
                });
            }
        }).start();
    }

    private List<SanPham> parseApiResponse(String jsonResponse) {
        List<SanPham> items = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);

                // Skip deleted items
                if (item.optBoolean("isdeleted", false)) {
                    continue;
                }

                SanPham sanPham = new SanPham(
                        String.valueOf(item.getInt("id")),
                        item.getString("itemName"),
                        item.getString("itemType"),
                        item.getDouble("itemCprice"),
                        item.getDouble("itemSprice"),
                        (int) item.getDouble("instock")
                );
                items.add(sanPham);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    // Rest of your existing methods remain exactly the same
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

            stage.showAndWait();

            boxThemSanPham.getStyleClass().remove("selected");
            boxThemSanPham.setScaleX(1.0);
            boxThemSanPham.setScaleY(1.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleXoaSanPham() {
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

            stage.showAndWait();

            boxXoaSanPham.getStyleClass().remove("selected");
            boxXoaSanPham.setScaleX(1.0);
            boxXoaSanPham.setScaleY(1.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}