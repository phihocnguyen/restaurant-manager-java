package Controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import model.Customer;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ManagerCustomer {

    @FXML
    private StackPane contentArea;

    @FXML
    private HBox boxThemThongTin;

    @FXML
    private HBox boxSuaThongTin;

    @FXML
    private HBox boxXoaThongTin;


    @FXML
    private TableView<Customer> tableCustomer;
    @FXML private TableColumn<Customer, Integer> colMaKH;
    @FXML private TableColumn<Customer, String> colTenKH;
    @FXML private TableColumn<Customer, String> colDiaChi;
    @FXML private TableColumn<Customer, String> colSDT;
    @FXML private TableColumn<Customer, String> colCCCD;
    @FXML private TableColumn<Customer, String> colEmail;


    @FXML
    public void initialize() {
        autoResizeTable();
        setupTableColumns();
        loadCustomerData();
    }

    private void setupTableColumns() {
        colMaKH.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTenKH.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDiaChi.setCellValueFactory(new PropertyValueFactory<>("address"));
        colSDT.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colCCCD.setCellValueFactory(new PropertyValueFactory<>("cccd"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    private void loadCustomerData() {
        Thread thread = new Thread(() -> {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("http://localhost:8080/customers")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    System.err.println("Unexpected code " + response);
                    return;
                }

                String responseBody = response.body().string();

                ObjectMapper objectMapper = new ObjectMapper();
                List<Customer> customers = objectMapper.readValue(
                        responseBody, new TypeReference<List<Customer>>() {}
                );

                // Đổ dữ liệu vào TableView trên UI thread
                Platform.runLater(() -> {
                    ObservableList<Customer> customerList = FXCollections.observableArrayList(customers);
                    tableCustomer.setItems(customerList);
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        thread.setDaemon(true);
        thread.start();
    }


    public void showOverview() {
        try {
            Parent fxml = FXMLLoader.load(getClass().getResource("../views/manager_customer.fxml"));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(fxml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void autoResizeTable() {
        tableCustomer.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        int columnCount = tableCustomer.getColumns().size();
        for (TableColumn<?, ?> column : tableCustomer.getColumns()) {
            column.prefWidthProperty().bind(tableCustomer.widthProperty().divide(columnCount));
        }
    }

    @FXML
    private void handleThemThongTin() {
// Animation click nhẹ
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), boxThemThongTin);
        scaleTransition.setToX(0.95);
        scaleTransition.setToY(0.95);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setCycleCount(2);
        scaleTransition.play();

        scaleTransition.setOnFinished(event -> {
            boxThemThongTin.getStyleClass().add("selected");
        });

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/customer_add.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.centerOnScreen();

            stage.showAndWait(); // block luồng tại đây -> chờ user đóng

            // Sau khi user đóng AddToMenu -> remove trạng thái selected
            boxThemThongTin.getStyleClass().remove("selected"); // <-- thêm dòng này
            boxThemThongTin.setScaleX(1.0);
            boxThemThongTin.setScaleY(1.0);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @FXML
    private void handleSuaThongTin() {
        // Animation click nhẹ
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), boxSuaThongTin);
        scaleTransition.setToX(0.95);
        scaleTransition.setToY(0.95);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setCycleCount(2);
        scaleTransition.play();

        scaleTransition.setOnFinished(event -> {
            boxSuaThongTin.getStyleClass().add("selected");
        });
        Customer selected = tableCustomer.getSelectionModel().getSelectedItem();
        if (selected == null) {
            System.out.println("Vui lòng chọn một khách hàng để sửa.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/customer_add.fxml"));
            Parent root = loader.load();

            CustomerAdd controller = loader.getController();
            controller.setCustomer(selected);

            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.centerOnScreen();

            stage.showAndWait(); // block luồng tại đây -> chờ user đóng

            // Sau khi user đóng AddToMenu -> remove trạng thái selected
            boxSuaThongTin.getStyleClass().remove("selected"); // <-- thêm dòng này
            boxSuaThongTin.setScaleX(1.0);
            boxSuaThongTin.setScaleY(1.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleXoaThongTin() {
// Animation click nhẹ
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), boxXoaThongTin);
        scaleTransition.setToX(0.95);
        scaleTransition.setToY(0.95);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setCycleCount(2);
        scaleTransition.play();

        scaleTransition.setOnFinished(event -> {
            boxXoaThongTin.getStyleClass().add("selected");
        });

        Customer selected = tableCustomer.getSelectionModel().getSelectedItem();
        if (selected == null) {
            System.out.println("Vui lòng chọn một khách hàng để sửa.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/confirm_deleted.fxml"));
            Parent root = loader.load();

            ConfirmDeletedController controller = loader.getController();
            controller.setCustomerToDelete(selected);

            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.centerOnScreen();

            stage.showAndWait(); // block luồng tại đây -> chờ user đóng

            // Sau khi user đóng AddToMenu -> remove trạng thái selected
            boxXoaThongTin.getStyleClass().remove("selected"); // <-- thêm dòng này
            boxXoaThongTin.setScaleX(1.0);
            boxXoaThongTin.setScaleY(1.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
