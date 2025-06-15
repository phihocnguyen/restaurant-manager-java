package com.restaurant.controller;

import com.restaurant.dto.MenuItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/sales")
public class SalesController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${backend.api.url}") // Assuming you have this property in application.properties
    private String backendApiUrl;

    @GetMapping
    public String salesPage(Model model) {
        model.addAttribute("title", "Bán hàng - Restaurant Manager");
        return "sales";
    }

    @GetMapping("/tables")
    public String salesTablesPage(Model model) {
        try {
            // Get tables from backend
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                backendApiUrl + "/tables",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );
            
            List<Map<String, Object>> tables = response.getBody();
            
            if (tables == null) {
                tables = new ArrayList<>();
            }

            // Sort tables by ID and ensure tabNum is properly mapped
            tables.sort((t1, t2) -> {
                Integer id1 = (Integer) t1.get("id");
                Integer id2 = (Integer) t2.get("id");
                if (id1 == null && id2 == null) return 0;
                if (id1 == null) return -1;
                if (id2 == null) return 1;
                return id1.compareTo(id2);
            });

            // Ensure tabNum is properly mapped
            tables.forEach(table -> {
                if (table.get("tabNum") == null) {
                    table.put("tabNum", 0); // Default value if null
                }
            });
            
            model.addAttribute("tables", tables);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("tables", new ArrayList<>());
        }
        
        model.addAttribute("title", "Quản lý bàn - Restaurant Manager");
        return "sales/tables";
    }

    @GetMapping("/api/tables")
    @ResponseBody
    public ResponseEntity<List> getAllTables() {
        String url = backendApiUrl + "/tables";
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    @GetMapping("/api/tables/{id}")
    @ResponseBody
    public ResponseEntity<Map> getTableById(@PathVariable int id) {
        String url = backendApiUrl + "/tables/" + id;
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    @PutMapping("/api/tables/{id}")
    @ResponseBody
    public ResponseEntity<?> updateTable(@PathVariable int id, @RequestBody Map<String, Object> tableData) {
        String url = backendApiUrl + "/tables/" + id;
        restTemplate.put(url, tableData);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/tables/{id}/status")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateTableStatus(
            @PathVariable String id,
            @RequestBody Map<String, Object> statusUpdate) {
        
        // Get current table data first
        ResponseEntity<Map<String, Object>> currentTableResponse = restTemplate.exchange(
                backendApiUrl + "/tables/" + id,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );
        
        Map<String, Object> currentTable = currentTableResponse.getBody();
        if (currentTable != null) {
            // Preserve tabNum from current table data
            statusUpdate.put("tabNum", currentTable.get("tabNum"));
        }
        
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                backendApiUrl + "/tables/" + id + "/status",
                HttpMethod.PUT,
                new org.springframework.http.HttpEntity<>(statusUpdate),
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    // API endpoint to get table orders
    @GetMapping("/api/tables/{id}/orders")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getTableOrders(@PathVariable String id) {
        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                backendApiUrl + "/tables/" + id + "/orders",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    // API endpoint to add order to table
    @PostMapping("/api/tables/{id}/orders")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addTableOrder(
            @PathVariable String id,
            @RequestBody Map<String, Object> order) {
        
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                backendApiUrl + "/tables/" + id + "/orders",
                HttpMethod.POST,
                new org.springframework.http.HttpEntity<>(order),
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    // Items routes
    @GetMapping("/items")
    public String items(Model model) {
        try {
            // Get items
            ResponseEntity<List<MenuItemDto>> itemsResponse = restTemplate.exchange(
                backendApiUrl + "/items",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<MenuItemDto>>() {}
            );
            
            // Get tables
            ResponseEntity<List<Map<String, Object>>> tablesResponse = restTemplate.exchange(
                backendApiUrl + "/tables",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );
            
            List<MenuItemDto> items = itemsResponse.getBody();
            List<Map<String, Object>> tables = tablesResponse.getBody();
            
            model.addAttribute("items", items);
            model.addAttribute("tables", tables);
            model.addAttribute("title", "Thực đơn - Restaurant Manager");
            model.addAttribute("activeTab", "food");
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("items", new ArrayList<>());
            model.addAttribute("tables", new ArrayList<>());
            model.addAttribute("title", "Thực đơn - Restaurant Manager");
            model.addAttribute("activeTab", "food");
        }
        
        return "sales/items";
    }

    @GetMapping("/items/food")
    public String foodItemsPage(Model model) {
        model.addAttribute("title", "Món ăn - Restaurant Manager");
        model.addAttribute("activeTab", "food");
        return "sales/items";
    }

    @GetMapping("/items/drink")
    public String drinkItemsPage(Model model) {
        model.addAttribute("title", "Nước uống - Restaurant Manager");
        model.addAttribute("activeTab", "drink");
        return "sales/items";
    }

    @GetMapping("/items/other")
    public String otherItemsPage(Model model) {
        model.addAttribute("title", "Món khác - Restaurant Manager");
        model.addAttribute("activeTab", "other");
        return "sales/items";
    }

    // History route
    @GetMapping("/history")
    public String historyPage(Model model) {
        model.addAttribute("title", "Lịch sử đơn hàng - Restaurant Manager");
        model.addAttribute("activeTab", "history");
        return "sales/history";
    }

    // API endpoint to get online orders
    @GetMapping("/api/online-orders")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getOnlineOrders(@RequestParam(required = false) String status) {
        String url = status != null ? 
            backendApiUrl + "/orders/status/" + status :
            backendApiUrl + "/orders";
            
        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    // API endpoint to get a single online order by ID
    @GetMapping("/api/online-orders/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getOnlineOrderById(@PathVariable Long id) {
        String url = backendApiUrl + "/orders/" + id;

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<Map<String, Object>>() {}
        );
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    // API endpoint to update online order status
    @PutMapping("/api/online-orders/{id}/status")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) Integer employeeId) {
        String url = backendApiUrl + "/orders/" + id + "/status?newStatus=" + status;
        
        // Add employeeId to the URL if it's provided
        if (employeeId != null) {
            url += "&employeeId=" + employeeId;
        }
        
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
            url,
            HttpMethod.PUT,
            null,
            new ParameterizedTypeReference<Map<String, Object>>() {}
        );
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    // API endpoint to get online orders with status other than PENDING
    @GetMapping("/api/online-orders/history")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getOnlineOrdersHistory() {
        String url = backendApiUrl + "/orders";

        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );

        List<Map<String, Object>> allOrders = response.getBody();
        List<Map<String, Object>> historyOrders = new ArrayList<>();

        if (allOrders != null) {
            for (Map<String, Object> order : allOrders) {
                String status = (String) order.get("status");
                if (!"PENDING".equals(status)) {
                    historyOrders.add(order);
                }
            }
        }

        return ResponseEntity.status(response.getStatusCode()).body(historyOrders);
    }
} 