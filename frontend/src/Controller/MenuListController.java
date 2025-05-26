package Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.TilePane;
import model.MenuItem;
import okhttp3.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class MenuListController {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TilePane menuContainer;

    private List<MenuItem> allItems;
    private String currentFilter = "all"; // all, food, drink, other

    private static final String API_URL = "http://localhost:8080/items";
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @FXML
    public void initialize() {
        setupContainer();
        refreshMenuItems();
    }

    private void setupContainer() {
        if (menuContainer != null) {
            menuContainer.setPrefColumns(4);
            menuContainer.setHgap(20);
            menuContainer.setVgap(20);
            menuContainer.setStyle("-fx-padding: 20;");
        }
    }

    public void refreshMenuItems() {
        Request request = new Request.Builder()
                .url(API_URL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    System.err.println("Lỗi khi tải danh sách món ăn: " + e.getMessage());
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    MenuItem[] items = objectMapper.readValue(responseBody, MenuItem[].class);
                    allItems = Arrays.asList(items);
                    Platform.runLater(() -> displayItems(allItems));
                } else {
                    System.err.println("Lỗi API: " + response.code());
                }
            }
        });
    }

    private void displayItems(List<MenuItem> items) {
        if (menuContainer == null) return;
        menuContainer.getChildren().clear();

        List<MenuItem> filtered = filterItems(items);

        for (MenuItem item : filtered) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/item_menu.fxml"));
                Node itemNode = loader.load();

                ItemMenu controller = loader.getController();
                controller.setData(item);

                menuContainer.getChildren().add(itemNode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<MenuItem> filterItems(List<MenuItem> items) {
        return items.stream()
                .filter(item -> !item.isIsdeleted())
                .filter(item -> {
                    switch (currentFilter) {
                        case "food":
                            return "food".equalsIgnoreCase(item.getItemType());
                        case "drink":
                            return "drink".equalsIgnoreCase(item.getItemType());
                        case "other":
                            return "other".equalsIgnoreCase(item.getItemType());
                        case "all":
                        default:
                            return true;
                    }
                })
                .collect(Collectors.toList());
    }

    public void setFilter(String filter) {
        this.currentFilter = filter;
        if (allItems != null) {
            displayItems(allItems);
        }
    }
}
