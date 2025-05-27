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

@Controller
@RequestMapping("/manager")
public class ManagerController {

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("title", "Dashboard - Restaurant Manager");
        model.addAttribute("activeTab", "dashboard");
        return "manager/dashboard";
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
        // Fetch menu items to display background list
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/items";
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
        List<Map<String, Object>> items = response.getBody();
        model.addAttribute("items", items);

        model.addAttribute("title", "Thêm món mới - Restaurant Manager");
        model.addAttribute("activeTab", "menu");
        model.addAttribute("showAdd", true);
        model.addAttribute("showEdit", false);
        model.addAttribute("editItem", new MenuItemDto()); // Empty DTO for new item form

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
    public String addMenu(@ModelAttribute MenuItemDto item, RedirectAttributes redirectAttributes) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/items";
        restTemplate.postForEntity(url, item, Void.class);
        redirectAttributes.addFlashAttribute("success", "Thêm món thành công!");
        return "redirect:/manager/menu";
    }

    @PostMapping("/menu/edit/{id}")
    public String editMenu(@PathVariable int id, @ModelAttribute MenuItemDto item, RedirectAttributes redirectAttributes) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/items/" + id;
        restTemplate.patchForObject(url, item, Void.class);
        redirectAttributes.addFlashAttribute("success", "Cập nhật thành công!");
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

    @GetMapping("/tables")
    public String tables(Model model) {
        model.addAttribute("title", "Quản lý Bàn - Restaurant Manager");
        model.addAttribute("activeTab", "tables");
        return "manager/tables";
    }

    @GetMapping("/customers")
    public String customers(Model model) {
        model.addAttribute("title", "Quản lý Khách hàng - Restaurant Manager");
        model.addAttribute("activeTab", "customers");
        return "manager/customers";
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
} 