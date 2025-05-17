package Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import model.Employee;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ManagerStaff {

    @FXML
    private StackPane contentArea;

    @FXML
    private HBox boxThemThongTin;

    @FXML
    private HBox boxSuaThongTin;

    @FXML
    private HBox boxXoaThongTin;

    @FXML private TableColumn<Employee, String> colMaNV;
    @FXML private TableColumn<Employee, String> colTenNV;
    @FXML private TableColumn<Employee, String> colDiaChi;
    @FXML private TableColumn<Employee, String> colSDT;
    @FXML private TableColumn<Employee, String> colCCCD;
    @FXML private TableColumn<Employee, String> colViTri;
    @FXML private TableColumn<Employee, Number> colEmail; // Lương



    @FXML
    private TableView<Employee> staffView;



    @FXML
    public void initialize() {

        autoResizeTable();
        setupTableColumns();
        fetchAndDisplayEmployees();

    }

    public void showOverview() {
        try {
            Parent fxml = FXMLLoader.load(getClass().getResource("../views/manager_staff.fxml"));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(fxml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupTableColumns() {
        colMaNV.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(String.valueOf(data.getValue().getId())));
        colTenNV.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        colDiaChi.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getAddress()));
        colSDT.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPhone()));
        colCCCD.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCccd()));
        colViTri.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getRole()));
        colEmail.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getSalary()));
    }



    private void autoResizeTable() {
        staffView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        int columnCount = staffView.getColumns().size();
        for (TableColumn<?, ?> column : staffView.getColumns()) {
            column.prefWidthProperty().bind(staffView.widthProperty().divide(columnCount));
        }
    }

    private void fetchAndDisplayEmployees() {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("http://localhost:8080/employees")
                        .build();

                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                ObjectMapper mapper = new ObjectMapper();
                List<Employee> employees = Arrays.asList(mapper.readValue(response.body().string(), Employee[].class));

                javafx.application.Platform.runLater(() -> {
                    staffView.getItems().setAll(employees.toArray(new Employee[0]));
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/staff_add.fxml"));
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

        try {
            Employee selected = staffView.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/staff_add.fxml"));
            Parent root = loader.load();

            StaffAdd controller = loader.getController();
            controller.setEmployee(selected);

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

        try {
            Employee selected = staffView.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/confirm_deleted.fxml"));
            Parent root = loader.load();

            ConfirmDeletedController confirmDeletedController = loader.getController();
            confirmDeletedController.setEmployeeToDelete(selected);

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
