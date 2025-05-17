package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import model.Employee;
import model.Ingredient;
import okhttp3.*;

import java.io.IOException;

public class ConfirmDeletedController {

    @FXML private Button btnHuy;
    @FXML private Button btnXoa;

    private Ingredient ingredientToDelete;

    private Employee employeeToDelete;
    private final OkHttpClient httpClient = new OkHttpClient();

    public void setIngredientToDelete(Ingredient ingredient) {
        this.ingredientToDelete = ingredient;
    }

    public void setEmployeeToDelete(Employee employee) {
        this.employeeToDelete = employee;
    }

    @FXML
    private void handleHuy() {
        Stage stage = (Stage) btnHuy.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleXacNhan() {
        if (ingredientToDelete == null && employeeToDelete == null) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không có thông tin để xóa");
            return;
        }

        String url;
        if (ingredientToDelete != null) {
            url = "http://localhost:8080/ingredients/" + ingredientToDelete.getId();
        } else {
            url = "http://localhost:8080/employees/" + employeeToDelete.getId();
        }

        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                javafx.application.Platform.runLater(() ->
                        showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể xóa: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                javafx.application.Platform.runLater(() -> {
                    if (response.isSuccessful()) {
                        showAlert(Alert.AlertType.INFORMATION, "Thành công",
                                (ingredientToDelete != null ? "Xóa nguyên liệu" : "Xóa nhân viên") + " thành công");
                        Stage stage = (Stage) btnXoa.getScene().getWindow();
                        stage.close();
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Lỗi",
                                "Xóa thất bại. Mã lỗi: " + response.code());
                    }
                });
            }
        });
    }


    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type, content, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
