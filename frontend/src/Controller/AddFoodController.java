package Controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.converter.DoubleStringConverter;
import model.Ingredient;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AddFoodController {

    @FXML
    private TextField txtSoLuong;

    @FXML
    private ComboBox<Ingredient> cbNguyenLieu;

    @FXML
    private TableView<Ingredient> tableNguyenLieu;

    @FXML
    private TableColumn<Ingredient, String> colTenNguyenLieu;

    @FXML
    private TableColumn<Ingredient, Double> colSoLuong;

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @FXML
    public Button btnLuuCongThuc;
    @FXML
    public Button btnThemNguyenLieu;
    @FXML
    public Button btnLuuXoaNguyenLieu;

    @FXML
    private VBox contentArea; // Hoặc VBox contentArea cũng được nếu bạn dùng VBox


    @FXML
    public void initialize() {
        colTenNguyenLieu.setCellValueFactory(cellData -> cellData.getValue().ingreNameProperty());
        colSoLuong.setCellValueFactory(new PropertyValueFactory<>("instockKg"));
        colSoLuong.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        colSoLuong.setOnEditCommit(event -> {
            Ingredient ingredient = event.getRowValue();
            ingredient.setInstockKg(event.getNewValue());
        });

        tableNguyenLieu.setEditable(true);
        loadIngredients();
    }

    private void loadIngredients() {
        new Thread(() -> {
            try {
                Request request = new Request.Builder()
                        .url("http://localhost:8080/ingredients")
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful() && response.body() != null) {
                        String json = response.body().string();

                        List<Ingredient> ingredients = objectMapper.readValue(
                                json,
                                new TypeReference<List<Ingredient>>() {}
                        );

                        // Lọc các nguyên liệu chưa bị xoá
                        List<Ingredient> filtered = ingredients.stream()
                                .filter(ingredient -> !ingredient.isIsdeleted())
                                .toList();

                        Platform.runLater(() -> {
                            cbNguyenLieu.getItems().clear();
                            cbNguyenLieu.getItems().addAll(filtered);
                        });
                    } else {
                        System.err.println("Failed to fetch ingredients: " + response.code());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    public void handleLuuCongThuc(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/add_other.fxml"));
            Parent newContent = loader.load();

            contentArea.getChildren().clear(); // Chỉ clear vùng content thôi
            contentArea.getChildren().add(newContent); // Add giao diện mới vô

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleThemNguyenLieu(MouseEvent event) {
        Ingredient selected = cbNguyenLieu.getSelectionModel().getSelectedItem();

        if (selected == null) {
            System.out.println("Vui lòng chọn một nguyên liệu.");
            return;
        }

        // Lấy số lượng nhập từ TextField, mặc định là 1.0 nếu không hợp lệ
        double quantityToAdd = 1.0;
        try {
            String text = txtSoLuong.getText();
            if (text != null && !text.trim().isEmpty()) {
                quantityToAdd = Double.parseDouble(text.trim());
                if (quantityToAdd <= 0) {
                    System.out.println("Số lượng phải lớn hơn 0. Mặc định dùng 1.0");
                    quantityToAdd = 1.0;
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Số lượng không hợp lệ, mặc định dùng 1.0");
        }

        // Tìm nguyên liệu đã có trong bảng
        Ingredient existing = tableNguyenLieu.getItems().stream()
                .filter(item -> item.getId() == selected.getId())
                .findFirst()
                .orElse(null);

        if (existing == null) {
            // Thêm nguyên liệu mới với số lượng lấy từ TextField
            Ingredient clone = new Ingredient(
                    selected.getId(),
                    selected.getIngreName(),
                    quantityToAdd,
                    selected.getIngrePrice(),
                    selected.isIsdeleted()
            );
            tableNguyenLieu.getItems().add(clone);
        } else {
            // Tăng số lượng nguyên liệu đã có bằng số lượng lấy từ TextField
            existing.setInstockKg(existing.getInstockKg() + quantityToAdd);
            tableNguyenLieu.refresh();
        }

        // Clear hoặc reset số lượng input nếu muốn
        txtSoLuong.clear();
    }




    @FXML
    public void handleXoaNguyenLieu(MouseEvent event) {
        Ingredient selected = tableNguyenLieu.getSelectionModel().getSelectedItem();
        if (selected != null) {
            tableNguyenLieu.getItems().remove(selected);
        } else {
            System.out.println("Vui lòng chọn một nguyên liệu để xóa.");
        }
    }

}