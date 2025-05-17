package Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Employee;
import okhttp3.*;
import Class.EmployeeRequest;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StaffAdd {
    @FXML
    private ImageView exitButton;
    @FXML private TextField name;
    @FXML private TextField phone;
    @FXML private TextField address;
    @FXML private TextField cccd;
    @FXML private TextField role;
    @FXML private TextField salary;

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private Employee currentEmployee;

    public void setEmployee(Employee employee) {
        this.currentEmployee = employee;
        name.setText(employee.getName());
        address.setText(employee.getAddress());
        phone.setText(employee.getPhone());
        cccd.setText(employee.getCccd());
        role.setText(employee.getRole());
        salary.setText(String.valueOf(employee.getSalary()));
    }

    @FXML
    private void onExitButtonClick() {

        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();

    }
    @FXML
    private void onConfirmButtonClick() {
        EmployeeRequest request = new EmployeeRequest();
        request.name = name.getText();
        request.phone = phone.getText();
        request.address = address.getText();
        request.cccd = cccd.getText();
        request.role = role.getText();
        request.salary = Double.parseDouble(salary.getText());
        request.workedDays = 0;
        request.startDate = Instant.now().toString();

        try {
            String json = objectMapper.writeValueAsString(request);
            RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

            String url;
            Request httpRequest;

            if (currentEmployee != null) {
                // Chế độ sửa → PUT /employees/{id}
                url = "http://localhost:8080/employees/" + currentEmployee.getId();
                httpRequest = new Request.Builder()
                        .url(url)
                        .put(body)
                        .build();
            } else {
                // Chế độ thêm mới → POST /employees
                url = "http://localhost:8080/employees";
                httpRequest = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
            }

            client.newCall(httpRequest).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("Lỗi gửi API: " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        System.out.println((currentEmployee != null ? "Cập nhật" : "Tạo") + " nhân viên thành công!");
                    } else {
                        System.out.println("Lỗi: " + response.code() + " - " + response.body().string());
                    }
                }
            });

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thành công");
            alert.setHeaderText(null);
            alert.setContentText((currentEmployee != null ? "Cập nhật" : "Tạo") + " nhân viên thành công!");

            alert.setOnHidden(e -> onExitButtonClick());
            alert.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
