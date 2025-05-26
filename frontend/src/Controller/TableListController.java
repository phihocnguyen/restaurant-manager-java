package Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import model.Table;
import okhttp3.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TableListController {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TilePane tableContainer;

    private List<Table> allTables;
    private String currentFilter = "all"; // all, active, empty, booked

    private static final String API_URL = "http://localhost:8080/tables";


    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @FXML
    public void initialize() {
        setupTableContainer();
        loadTablesFromAPI();
    }

    private void setupTableContainer() {
        if (tableContainer != null) {
            tableContainer.setPrefColumns(4); // 4 cột
            tableContainer.setHgap(20); // Khoảng cách ngang
            tableContainer.setVgap(20); // Khoảng cách dọc
            tableContainer.setStyle("-fx-padding: 20;");
        }
    }

    public void loadTablesFromAPI() {
        Request request = new Request.Builder()
                .url(API_URL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // Không gọi loadSampleData(), chỉ log lỗi
                Platform.runLater(() -> {
                    // Optionally, bạn có thể hiển thị thông báo lỗi trong UI
                    System.err.println("Failed to fetch table data: " + e.getMessage());
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    Table[] tablesArray = objectMapper.readValue(responseBody, Table[].class);
                    allTables = Arrays.asList(tablesArray);
                    Platform.runLater(() -> displayTables(allTables));
                } else {
                    System.err.println("API Error: " + response.code());
                    Platform.runLater(() -> {
                        // Có thể hiển thị thông báo lỗi trong UI
                        System.err.println("No data displayed due to API error.");
                    });
                }
            }
        });
    }


    private void displayTables(List<Table> tables) {
        if (tableContainer == null) return;

        tableContainer.getChildren().clear();

        List<Table> filteredTables = filterTables(tables);

        for (Table table : filteredTables) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/item_table.fxml"));
                Node tableItem = loader.load();

                ItemTable controller = loader.getController();
                controller.setTableData(table);

                tableContainer.getChildren().add(tableItem);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Table> filterTables(List<Table> tables) {
        return tables.stream()
                .filter(table -> !table.isDeleted()) // Loại bỏ bàn đã xóa
                .filter(table -> {
                    switch (currentFilter) {
                        case "active":
                            return table.isActive();
                        case "empty":
                            return !table.isActive() && !table.isBooked();
                        case "booked":
                            return table.isBooked();
                        case "all":
                        default:
                            return true;
                    }
                })
                .collect(Collectors.toList());
    }

    // Method để thay đổi filter từ bên ngoài
    public void setFilter(String filter) {
        this.currentFilter = filter;
        if (allTables != null) {
            displayTables(allTables);
        }
    }

    // Method để refresh dữ liệu
    public void refreshTables() {
        loadTablesFromAPI();
    }

    // Getter cho danh sách bàn
    public List<Table> getAllTables() {
        return allTables;
    }
}