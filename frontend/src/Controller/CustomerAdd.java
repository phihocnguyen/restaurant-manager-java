package Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Customer;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;

public class CustomerAdd {

    @FXML
    private TextField name;
    @FXML
    private TextField phone;
    @FXML
    private TextField address;
    @FXML
    private TextField cccd;
    @FXML
    private TextField email;

    private Customer customer;

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public void setCustomer(Customer customer) {
        this.customer = customer;
        if (customer != null) {
            name.setText(customer.getName());
            phone.setText(customer.getPhone());
            address.setText(customer.getAddress());
            cccd.setText(customer.getCccd());
            email.setText(customer.getEmail());
        }
    }

    @FXML
    private void handleConfirm() {
        try {
            String nameText = name.getText();
            String phoneText = phone.getText();
            String addressText = address.getText();
            String cccdText = cccd.getText();
            String emailText = email.getText();

            // Client-side validation
            if (nameText.isEmpty() || phoneText.isEmpty() || addressText.isEmpty() ||
                    cccdText.isEmpty() || emailText.isEmpty()) {
                showErrorAlert("Dữ liệu không hợp lệ", "Vui lòng điền đầy đủ tất cả các trường.");
                return;
            }

            // Validate CCCD: must be exactly 12 digits
            if (!cccdText.matches("^[0-9]{12}$")) {
                showErrorAlert("Dữ liệu không hợp lệ", "CCCD phải là chuỗi 12 chữ số.");
                return;
            }

            // Validate email format (basic check)
            if (!emailText.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                showErrorAlert("Dữ liệu không hợp lệ", "Email không đúng định dạng.");
                return;
            }

            // Create JSON using Jackson
            var jsonObject = new HashMap<String, String>();
            jsonObject.put("name", nameText);
            jsonObject.put("cccd", cccdText);
            jsonObject.put("email", emailText);
            jsonObject.put("phone", phoneText);
            jsonObject.put("address", addressText);
            String json = mapper.writeValueAsString(jsonObject);
            System.out.println("Sending JSON: " + json);

            RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
            Request.Builder requestBuilder = new Request.Builder();

            if (customer != null) {
                requestBuilder.url("http://localhost:8080/customers/" + customer.getId())
                        .put(body);
            } else {
                requestBuilder.url("http://localhost:8080/customers")
                        .post(body);
            }

            Request request = requestBuilder.build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("Request failed: " + e.getMessage());
                    Platform.runLater(() -> showErrorAlert("Lỗi kết nối",
                            "Đã xảy ra lỗi khi gửi yêu cầu: " + e.getMessage()));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseBody = response.body() != null ? response.body().string() : "No response body";
                    System.out.println("Response code: " + response.code());
                    System.out.println("Response body: " + responseBody);

                    Platform.runLater(() -> {
                        Alert alert;
                        if (response.isSuccessful()) {
                            alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Thành công");
                            alert.setHeaderText(customer != null ? "Cập nhật khách hàng thành công" : "Thêm khách hàng thành công");
                            alert.setContentText("Khách hàng đã được " + (customer != null ? "cập nhật" : "thêm") + " vào hệ thống.");
                            alert.showAndWait();
                            Stage stage = (Stage) name.getScene().getWindow();
                            stage.close();
                        } else {
                            String errorMessage = parseErrorMessage(responseBody);
                            showErrorAlert(customer != null ? "Cập nhật khách hàng thất bại" : "Thêm khách hàng thất bại",
                                    "Mã lỗi: " + response.code() + "\nChi tiết: " + errorMessage);
                        }
                    });
                    response.close();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Lỗi xử lý", "Chi tiết lỗi: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) name.getScene().getWindow();
        stage.close();
    }

    private void showErrorAlert(String header, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(header);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }

    private String parseErrorMessage(String responseBody) {
        try {
            var jsonNode = mapper.readTree(responseBody);
            var errors = jsonNode.get("errors");
            if (errors != null && errors.isArray() && errors.size() > 0) {
                StringBuilder message = new StringBuilder();
                for (var error : errors) {
                    String field = error.get("field").asText();
                    String defaultMessage = error.get("defaultMessage").asText();
                    message.append(field).append(": ").append(defaultMessage).append("\n");
                }
                return message.toString();
            }
            return jsonNode.get("message") != null ? jsonNode.get("message").asText() : responseBody;
        } catch (Exception e) {
            return responseBody; // Fallback to raw response if parsing fails
        }
    }
}