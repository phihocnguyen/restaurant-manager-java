package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import okhttp3.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AddOther {

    @FXML
    private TextField txtTenSanPham;

    @FXML
    private TextField txtGiaVon;

    @FXML
    private TextField txtGiaBan;

    @FXML
    private TextField txtStock;

    @FXML
    private ImageView imgPreview;

    @FXML
    private Button btnChonAnh;

    @FXML
    private Button txtThemMonAn;

    private List<Object> copyList;

    private String type = "DRINK";

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    // Pre-fill the form with sample data when the controller is initialized
    @FXML
    public void initialize() {
        // Sample data to pre-fill the form
        txtTenSanPham.setText("Beef Noodles");
        txtGiaVon.setText("7.5");
        txtGiaBan.setText("10.0");
        txtStock.setText("50");
        // Note: Image is not set here, but you can load "beef_noodles.png" if needed
    }

    @FXML
    private void handleThemMonAn() {
        String tenSanPham = txtTenSanPham.getText();
        String giaVon = txtGiaVon.getText();
        String giaBan = txtGiaBan.getText();
        String stock = txtStock.getText();

        Image image = imgPreview.getImage();
        String imageName = image != null ? "beef_noodles.png" : null; // Placeholder for image name
        System.out.println(image);

        System.out.println("Thông tin món ăn:");
        System.out.println(getType());
        System.out.println("Tên sản phẩm: " + tenSanPham);
        System.out.println("Giá vốn: " + giaVon);
        System.out.println("Giá bán: " + giaBan);
        System.out.println("Số lượng trong kho: " + stock);

        if (image != null) {
            System.out.println("Ảnh đã được chọn: " + imageName);
        } else {
            System.out.println("Chưa chọn ảnh");
        }

        // Call the API with the data
        try {
            sendDataToApi(tenSanPham, giaVon, giaBan, stock, imageName);
        } catch (Exception e) {
            System.out.println("Error calling API: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void sendDataToApi(String tenSanPham, String giaVon, String giaBan, String stock, String imageName) throws Exception {
        // Define the data structures for JSON serialization
        class IngredientData {
            public int ingreId;
            public double ingreQuantityKg;

            public IngredientData(int ingreId, double ingreQuantityKg) {
                this.ingreId = ingreId;
                this.ingreQuantityKg = ingreQuantityKg;
            }
        }

        class RecipesData {
            public List<IngredientData> ingredients;

            public RecipesData(List<IngredientData> ingredients) {
                this.ingredients = ingredients;
            }
        }

        class MenuItemData {
            public String itemType;
            public String itemName;
            public String itemImg;
            public double itemCprice;
            public double itemSprice;
            public int instock;
            public boolean isdeleted;

            public MenuItemData(String itemType, String itemName, String itemImg, double itemCprice, double itemSprice, int instock, boolean isdeleted) {
                this.itemType = itemType;
                this.itemName = itemName;
                this.itemImg = itemImg;
                this.itemCprice = itemCprice;
                this.itemSprice = itemSprice;
                this.instock = instock;
                this.isdeleted = isdeleted;
            }
        }

        class RequestData {
            public MenuItemData menuItem;
            public RecipesData recipes;

            public RequestData(MenuItemData menuItem, RecipesData recipes) {
                this.menuItem = menuItem;
                this.recipes = recipes;
            }
        }

        // Prepare the ingredients list from copyList
        List<IngredientData> ingredients = new ArrayList<>();
        if (copyList != null) {
            for (Object obj : copyList) {
                if (obj instanceof Map<?, ?> map) {
                    Object ingreIdObj = map.get("id");
                    Object ingreQuantityKgObj = map.get("ingreKg");
                    if (ingreIdObj instanceof Integer && ingreQuantityKgObj instanceof Double) {
                        ingredients.add(new IngredientData((Integer) ingreIdObj, (Double) ingreQuantityKgObj));
                    }
                }
            }
        } else {
        }

        MenuItemData menuItem = new MenuItemData(
                type,
                tenSanPham,
                imageName != null ? imageName : "default.png",
                Double.parseDouble(giaVon),
                Double.parseDouble(giaBan),
                Integer.parseInt(stock),
                false
        );

        RecipesData recipes = new RecipesData(ingredients);

        RequestData requestData = new RequestData(menuItem, recipes);
        System.out.println(menuItem.itemName);
        System.out.println(menuItem.itemType);
        System.out.println(recipes.ingredients);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = type.equals("FOOD") ? objectMapper.writeValueAsString(requestData) : objectMapper.writeValueAsString(menuItem);

        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(jsonString, JSON);
        String URL = type.equals("FOOD") ? "http://localhost:8080/recipes/many" : "http://localhost:8080/items";
        Request request = new Request.Builder()
                .url(URL)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                System.out.println("API call successful: " + response.body().string());
            } else {
                System.out.println("API call failed: " + response.code() + " - " + response.body().string());
            }
        }
    }

    public void setCopyList(List<Object> copyList) {
        this.copyList = copyList;

        System.out.println("Received copyList in AddOther:");
        for (Object obj : copyList) {
            if (obj instanceof Map<?, ?> map) {
                Object id = map.get("id");
                System.out.println(id);
                Object kg = map.get("ingreKg");
                System.out.println(kg);
            }
        }
    }

    @FXML
    private void handleChonHinhAnh(javafx.scene.input.MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh sản phẩm");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(btnChonAnh.getScene().getWindow());

        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            imgPreview.setImage(image);
        }
    }
}