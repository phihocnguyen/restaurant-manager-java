package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Ingredient;
import okhttp3.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;

public class IngredientAdd {

    private Ingredient ingredient;

    @FXML
    private ImageView exitButton;

    @FXML
    private Button btnEditThongTin;

    @FXML
    private Button btnHuyThongTin;

    @FXML
    private TextField txtTenNL;

    @FXML
    private TextField txtGiaBan;

    @FXML
    private TextField txtSLT;

    private final OkHttpClient httpClient = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;

        // Gán dữ liệu vào các field trong form
        txtTenNL.setText(ingredient.getIngreName());
        txtGiaBan.setText(String.valueOf(ingredient.getIngrePrice()));
        txtSLT.setText(String.valueOf(ingredient.getInstockKg()));


    }

    @FXML
    private void onExitButtonClick() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void handleSubmit(MouseEvent event) {
        if (txtTenNL.getText().isEmpty() || txtGiaBan.getText().isEmpty() || txtSLT.getText().isEmpty()) {
            showAlert(AlertType.ERROR, "Lỗi", "Vui lòng điền đầy đủ thông tin");
            return;
        }

        try {
            ObjectNode requestBody = objectMapper.createObjectNode();
            requestBody.put("ingreName", txtTenNL.getText());
            requestBody.put("ingrePrice", Double.parseDouble(txtGiaBan.getText()));
            requestBody.put("instockKg", Double.parseDouble(txtSLT.getText()));

            String url;
            Request.Builder requestBuilder;

            if (ingredient != null) {
                // Sửa nguyên liệu (PUT)
                url = "http://localhost:8080/ingredients/" + ingredient.getId();
                requestBuilder = new Request.Builder()
                        .url(url)
                        .put(RequestBody.create(
                                objectMapper.writeValueAsString(requestBody),
                                MediaType.parse("application/json")));
            } else {
                // Thêm nguyên liệu (POST)
                url = "http://localhost:8080/ingredients";
                requestBuilder = new Request.Builder()
                        .url(url)
                        .post(RequestBody.create(
                                objectMapper.writeValueAsString(requestBody),
                                MediaType.parse("application/json")));
            }

            httpClient.newCall(requestBuilder.build()).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    javafx.application.Platform.runLater(() -> {
                        showAlert(AlertType.ERROR, "Lỗi", "Không thể kết nối đến server: " + e.getMessage());
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseBody = response.body().string();
                    System.out.println("Response: " + responseBody);

                    javafx.application.Platform.runLater(() -> {
                        if (response.isSuccessful()) {
                            String action = (ingredient != null) ? "Cập nhật" : "Thêm";
                            showAlert(AlertType.INFORMATION, "Thành công", action + " nguyên liệu thành công");
                            Stage stage = (Stage) btnEditThongTin.getScene().getWindow();
                            stage.close();
                        } else {
                            showAlert(AlertType.ERROR, "Lỗi", "Yêu cầu thất bại. Mã lỗi: " + response.code());
                        }
                    });
                }
            });

        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Lỗi", "Giá bán và số lượng phải là số");
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Lỗi", "Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    public void handleHuyThongTin(MouseEvent event) {
        // Clear input fields
        txtTenNL.clear();
        txtGiaBan.clear();
        txtSLT.clear();
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}