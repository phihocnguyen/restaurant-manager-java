package com.restaurant.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Map;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.restaurant.dto.MenuItemDto;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.HashMap;
import java.io.IOException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import java.math.BigDecimal;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import java.util.Comparator;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("title", "Dashboard - Restaurant Manager");
        model.addAttribute("activeTab", "dashboard");
        return "manager/dashboard";
    }

    @GetMapping("/tables")
    public String managerTablesPage(Model model) {
        model.addAttribute("title", "Quản lý bàn - Restaurant Manager");
        model.addAttribute("activeTab", "tables");
        return "manager/tables";
    }

    @GetMapping("/api/tables")
    @ResponseBody
    public ResponseEntity<List> getAllTables() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/tables";
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    @GetMapping("/api/tables/{id}")
    @ResponseBody
    public ResponseEntity<Map> getTableById(@PathVariable int id) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/tables/" + id;
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    @PostMapping("/api/tables")
    @ResponseBody
    public ResponseEntity<?> createTable(@RequestBody Map<String, Object> tableData) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/tables";
        ResponseEntity<?> response = restTemplate.postForEntity(url, tableData, Object.class);
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    @PutMapping("/api/tables/{id}")
    @ResponseBody
    public ResponseEntity<?> updateTable(@PathVariable int id, @RequestBody Map<String, Object> tableData) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/tables/" + id;
        restTemplate.put(url, tableData);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/tables/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteTable(@PathVariable int id) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/tables/" + id;
        restTemplate.delete(url);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/menu")
    public String menu(Model model) {
        model.addAttribute("title", "Quản lý Menu - Restaurant Manager");
        model.addAttribute("activeTab", "menu");

        // Fetch menu items from backend
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/items";
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
        List<Map<String, Object>> items = response.getBody();
        model.addAttribute("items", items);

        // Default state: modal is hidden
        model.addAttribute("showAdd", false);
        model.addAttribute("showEdit", false);
        model.addAttribute("editItem", new MenuItemDto());

        return "manager/menu";
    }

    @GetMapping("/menu/add")
    public String addMenuForm(Model model) {
        // Lấy lại danh sách để hiển thị nền
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/items";
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
        List<Map<String, Object>> items = response.getBody();
        model.addAttribute("items", items);
        model.addAttribute("title", "Thêm món mới - Restaurant Manager");
        model.addAttribute("activeTab", "menu");
        model.addAttribute("showAdd", true);
        model.addAttribute("showEdit", false);
        model.addAttribute("editItem", new MenuItemDto());
        // Lấy danh sách nguyên liệu
        String ingUrl = "http://localhost:8080/ingredients";
        ResponseEntity<List> ingResponse = restTemplate.getForEntity(ingUrl, List.class);
        List<Map<String, Object>> ingredientsList = ingResponse.getBody();
        model.addAttribute("ingredientsList", ingredientsList);
        return "manager/menu";
    }

    @GetMapping("/menu/edit/{id}")
    public String editMenuForm(@PathVariable int id, Model model) {
        // Fetch menu items to display background list
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/items";
        ResponseEntity<List> allItemsResponse = restTemplate.getForEntity(url, List.class);
        List<Map<String, Object>> items = allItemsResponse.getBody();
        model.addAttribute("items", items);

        // Fetch the specific item for editing
        String itemUrl = "http://localhost:8080/items/" + id;
        ResponseEntity<MenuItemDto> itemResponse = restTemplate.getForEntity(itemUrl, MenuItemDto.class);
        MenuItemDto editItem = itemResponse.getBody();

        model.addAttribute("title", "Chỉnh sửa món ăn - Restaurant Manager");
        model.addAttribute("activeTab", "menu");
        model.addAttribute("showAdd", false);
        model.addAttribute("showEdit", true);
        model.addAttribute("editItem", editItem); // DTO with item data

        return "manager/menu";
    }

    @PostMapping("/menu")
    public String addMenu(
            @RequestParam String itemType,
            @RequestParam String itemName,
            @RequestParam(value = "itemSprice", required = false) BigDecimal itemSprice,
            @RequestParam(value = "instock", required = false) Double instock,
            @RequestParam(value = "isdeleted", required = false) Boolean isdeleted,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "ingredientIds", required = false) List<Integer> ingredientIds,
            @RequestParam(value = "ingredientKgs", required = false) List<Double> ingredientKgs,
            RedirectAttributes redirectAttributes) throws IOException {

        RestTemplate restTemplate = new RestTemplate();

        if ("FOOD".equals(itemType)) {
            if (ingredientIds != null && !ingredientIds.isEmpty()) {
                MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<>();

                // Common menu fields
                multipartRequest.add("itemType", itemType);
                multipartRequest.add("itemName", itemName);
                if (itemSprice != null) multipartRequest.add("itemSprice", itemSprice);
                if (instock != null) multipartRequest.add("instock", instock);
                if (isdeleted != null) multipartRequest.add("isdeleted", isdeleted);
                if (image != null && !image.isEmpty()) {
                    multipartRequest.add("image", image.getResource());
                }

                if (ingredientIds.size() == 1) {
                    // Single recipe
                    multipartRequest.add("ingreId", ingredientIds.get(0));
                    multipartRequest.add("ingreQuantityKg", ingredientKgs != null && ingredientKgs.size() > 0 ? ingredientKgs.get(0) : 0.0);

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

                    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(multipartRequest, headers);
                    String url = "http://localhost:8080/recipes/one";
                    restTemplate.postForObject(url, requestEntity, Object.class);

                } else {
                    // Multiple recipes
                    for (int i = 0; i < ingredientIds.size(); i++) {
                        multipartRequest.add("ingreIds[]", ingredientIds.get(i));
                        multipartRequest.add("ingreQuantityKgs[]", ingredientKgs != null && i < ingredientKgs.size() ? ingredientKgs.get(i) : 0.0);
                    }

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

                    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(multipartRequest, headers);
                    String url = "http://localhost:8080/recipes/many";
                    restTemplate.postForObject(url, requestEntity, Object.class);
                }
            } else {
                redirectAttributes.addFlashAttribute("error", "Món ăn cần ít nhất một nguyên liệu!");
                return "redirect:/manager/menu/add";
            }
        } else {
            // Handle non-food items
            MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<>();
            multipartRequest.add("itemType", itemType);
            multipartRequest.add("itemName", itemName);
            if (itemSprice != null) multipartRequest.add("itemSprice", itemSprice);
            if (instock != null) multipartRequest.add("instock", instock);
            if (isdeleted != null) multipartRequest.add("isdeleted", isdeleted);
            if (image != null && !image.isEmpty()) {
                multipartRequest.add("image", image.getResource());
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(multipartRequest, headers);
            String url = "http://localhost:8080/items";
            restTemplate.postForObject(url, requestEntity, Object.class);
        }

        return "redirect:/manager/menu";
    }


    @PostMapping("/menu/delete/{id}")
    public String deleteMenu(@PathVariable int id, RedirectAttributes redirectAttributes) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/items/" + id;
        restTemplate.delete(url);
        redirectAttributes.addFlashAttribute("success", "Đã xóa món!");
        return "redirect:/manager/menu";
    }

    @GetMapping("/reports")
    public String reports(Model model) {
        model.addAttribute("title", "Báo cáo - Restaurant Manager");
        model.addAttribute("activeTab", "reports");
        return "manager/reports";
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        model.addAttribute("title", "Quản lý Đơn hàng - Restaurant Manager");
        model.addAttribute("activeTab", "orders");
        return "manager/orders";
    }

    @GetMapping("/settings")
    public String settings(Model model) {
        model.addAttribute("title", "Cài đặt - Restaurant Manager");
        model.addAttribute("activeTab", "settings");
        return "manager/settings";
    }

    @GetMapping("/ingredients")
    public String ingredients(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {
        model.addAttribute("title", "Quản lý Nguyên liệu - Restaurant Manager");
        model.addAttribute("activeTab", "ingredients");
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/ingredients?page=" + page;
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
        List<Map<String, Object>> ingredients = response.getBody();
        model.addAttribute("ingredients", ingredients);
        model.addAttribute("currentPage", page);
        // Giả lập tổng số trang (cần backend trả về nếu muốn chính xác)
        model.addAttribute("totalPages", 5);
        model.addAttribute("showAddIngre", false);
        model.addAttribute("showEditIngre", false);
        model.addAttribute("editIngre", null);
        return "manager/ingredients";
    }

    @GetMapping("/ingredients/add")
    public String addIngredientForm(Model model) {
        // Lấy lại danh sách để hiển thị nền
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/ingredients?page=1";
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
        List<Map<String, Object>> ingredients = response.getBody();
        model.addAttribute("ingredients", ingredients);
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPages", 5);
        model.addAttribute("showAddIngre", true);
        model.addAttribute("showEditIngre", false);
        model.addAttribute("editIngre", null);
        return "manager/ingredients";
    }

    @GetMapping("/ingredients/edit/{id}")
    public String editIngredientForm(@PathVariable int id, Model model) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/ingredients?page=1";
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
        List<Map<String, Object>> ingredients = response.getBody();
        // Lấy nguyên liệu cần sửa
        String ingUrl = "http://localhost:8080/ingredients/" + id;
        ResponseEntity<Map> ingResponse = restTemplate.getForEntity(ingUrl, Map.class);
        Map<String, Object> editIngre = ingResponse.getBody();
        model.addAttribute("ingredients", ingredients);
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPages", 5);
        model.addAttribute("showAddIngre", false);
        model.addAttribute("showEditIngre", true);
        model.addAttribute("editIngre", editIngre);
        return "manager/ingredients";
    }

    @PostMapping("/ingredients")
    public String addIngredient(@RequestParam String ingreName, @RequestParam Double ingrePrice, @RequestParam Double instockKg, RedirectAttributes redirectAttributes) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> body = new HashMap<>();
        body.put("ingreName", ingreName);
        body.put("ingrePrice", ingrePrice);
        body.put("instockKg", instockKg);
        restTemplate.postForEntity("http://localhost:8080/ingredients", body, Void.class);
        redirectAttributes.addFlashAttribute("success", "Thêm nguyên liệu thành công!");
        return "redirect:/manager/ingredients";
    }

    @PostMapping("/ingredients/edit/{id}")
    public String editIngredient(@PathVariable int id, @RequestParam String ingreName, @RequestParam Double ingrePrice, @RequestParam Double instockKg, RedirectAttributes redirectAttributes) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> body = new HashMap<>();
        body.put("ingreName", ingreName);
        body.put("ingrePrice", ingrePrice);
        body.put("instockKg", instockKg);
        restTemplate.patchForObject("http://localhost:8080/ingredients/" + id, body, Void.class);
        redirectAttributes.addFlashAttribute("success", "Cập nhật nguyên liệu thành công!");
        return "redirect:/manager/ingredients";
    }

    @PostMapping("/ingredients/delete/{id}")
    public String deleteIngredient(@PathVariable int id, RedirectAttributes redirectAttributes) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete("http://localhost:8080/ingredients/" + id);
        redirectAttributes.addFlashAttribute("success", "Đã xóa nguyên liệu!");
        return "redirect:/manager/ingredients";
    }

    @GetMapping("/sales/items")
    public String salesItemsPage(@RequestParam(value = "tableId", required = false) Integer tableId, Model model) {
        model.addAttribute("title", "Restro POS");

        // Fetch menu items from backend
        RestTemplate restTemplate = new RestTemplate();
        String urlItems = "http://localhost:8080/items";
        ResponseEntity<List> itemsResponse = restTemplate.getForEntity(urlItems, List.class);
        List<Map<String, Object>> items = itemsResponse.getBody();
        model.addAttribute("items", items);

        // Fetch tables from backend and sort by ID
        String urlTables = "http://localhost:8080/tables";
        ResponseEntity<List> tablesResponse = restTemplate.getForEntity(urlTables, List.class);
        List<Map<String, Object>> tables = tablesResponse.getBody();

        // Sort tables by ID
        if (tables != null) {
            tables.sort(Comparator.comparingInt(t -> (Integer) t.get("id")));
        }
        model.addAttribute("tables", tables);

        // Add tableId to the model if present (from URL parameter)
        if (tableId != null) {
            model.addAttribute("selectedTableId", tableId);
        }

        return "sales/items";
    }

} 