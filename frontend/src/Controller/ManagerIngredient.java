package Controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import model.Ingredient;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ManagerIngredient {

    private final OkHttpClient httpClient = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ObservableList<Ingredient> ingredientList = FXCollections.observableArrayList();

    @FXML private StackPane contentArea;
    @FXML private HBox boxThemSanPham;
    @FXML private HBox boxSuaSanPham;
    @FXML private HBox boxXoaSanPham;
    @FXML private TableView<Ingredient> tableIngredient;
    @FXML private TableColumn<Ingredient, Integer> colMaNL;
    @FXML private TableColumn<Ingredient, String> colTenNL;
    @FXML private TableColumn<Ingredient, Double> colGiaNhap;
    @FXML private TableColumn<Ingredient, Double> colTonKho;

    @FXML
    public void initialize() {
        setupTableColumns();
        autoResizeTable();
        loadIngredientsFromAPI();
    }

    private void setupTableColumns() {
        colMaNL.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTenNL.setCellValueFactory(new PropertyValueFactory<>("ingreName"));
        colGiaNhap.setCellValueFactory(new PropertyValueFactory<>("ingrePrice"));
        colTonKho.setCellValueFactory(new PropertyValueFactory<>("instockKg"));
        tableIngredient.setItems(ingredientList);
    }

    private void loadIngredientsFromAPI() {
        new Thread(() -> {
            Request request = new Request.Builder()
                    .url("http://localhost:8080/ingredients")
                    .header("Accept", "application/json")
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                String responseBody = Objects.requireNonNull(response.body()).string();
                List<Ingredient> ingredients = objectMapper.readValue(
                        responseBody,
                        new TypeReference<List<Ingredient>>() {}
                );

                javafx.application.Platform.runLater(() -> {
                    ingredientList.clear();
                    ingredientList.addAll(ingredients);
                    tableIngredient.refresh();
                });

            } catch (IOException e) {
                e.printStackTrace();
                // Xử lý lỗi ở đây, có thể hiển thị thông báo cho người dùng
                javafx.application.Platform.runLater(() -> {
                    // Hiển thị thông báo lỗi trên UI
                    System.err.println("Failed to load ingredients: " + e.getMessage());
                });
            }
        }).start();
    }

    // Các phương thức khác giữ nguyên như cũ
    private void autoResizeTable() {
        tableIngredient.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        int columnCount = tableIngredient.getColumns().size();
        for (TableColumn<?, ?> column : tableIngredient.getColumns()) {
            column.prefWidthProperty().bind(tableIngredient.widthProperty().divide(columnCount));
        }
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ingredient_add.fxml"));
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
            boxThemSanPham.setScaleX(1.0);
            boxThemSanPham.setScaleY(1.0);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void handleSuaSanPham() {
        // Animation click nhẹ
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), boxSuaSanPham);
        scaleTransition.setToX(0.95);
        scaleTransition.setToY(0.95);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setCycleCount(2);
        scaleTransition.play();

        scaleTransition.setOnFinished(event -> {
            boxSuaSanPham.getStyleClass().add("selected");
        });

        Ingredient selected = tableIngredient.getSelectionModel().getSelectedItem();
        if (selected == null) {
            System.out.println("Vui lòng chọn một nguyên liệu để sửa.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ingredient_add.fxml"));
            Parent root = loader.load();

            IngredientAdd controller = loader.getController();
            controller.setIngredient(selected);

            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.centerOnScreen();

            stage.showAndWait(); // block luồng tại đây -> chờ user đóng

            // Sau khi user đóng AddToMenu -> remove trạng thái selected
            boxSuaSanPham.getStyleClass().remove("selected"); // <-- thêm dòng này
            boxSuaSanPham.setScaleX(1.0);
            boxSuaSanPham.setScaleY(1.0);
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

        Ingredient selected = tableIngredient.getSelectionModel().getSelectedItem();
        if (selected == null) {
            System.out.println("Vui lòng chọn một nguyên liệu để xóa.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/confirm_deleted.fxml"));
            Parent root = loader.load();

            ConfirmDeletedController controller = loader.getController();
            controller.setIngredientToDelete(selected);

            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.centerOnScreen();

            stage.showAndWait(); // block luồng tại đây -> chờ user đóng

            // Sau khi user đóng AddToMenu -> remove trạng thái selected
            boxXoaSanPham.getStyleClass().remove("selected"); // <-- thêm dòng này
            boxXoaSanPham.setScaleX(1.0);
            boxXoaSanPham.setScaleY(1.0);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void showOverview() {
        try {
            Parent fxml = FXMLLoader.load(getClass().getResource("../views/manager_ingredient.fxml"));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(fxml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}