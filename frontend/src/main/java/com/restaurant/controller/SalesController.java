package com.restaurant.controller;

import com.restaurant.dto.MenuItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/sales")
public class SalesController {

    @Autowired
    private RestTemplate restTemplate;

    private final String API_URL = "http://localhost:8080";

    @GetMapping
    public String salesPage(Model model) {
        model.addAttribute("title", "Bán hàng - Restaurant Manager");
        return "sales";
    }

    // Tables routes
    @GetMapping("/tables")
    public String tablesPage(Model model) {
        model.addAttribute("title", "Quản lý bàn - Restaurant Manager");
        model.addAttribute("activeTab", "tables");
        return "sales/tables";
    }

    // API endpoint to get table details
    @GetMapping("/api/tables/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getTableDetails(@PathVariable String id) {
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                API_URL + "/tables/" + id,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    // API endpoint to update table status
    @PutMapping("/api/tables/{id}/status")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateTableStatus(
            @PathVariable String id,
            @RequestBody Map<String, Object> statusUpdate) {
        
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                API_URL + "/tables/" + id + "/status",
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
                API_URL + "/tables/" + id + "/orders",
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
                API_URL + "/tables/" + id + "/orders",
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
            ResponseEntity<List<MenuItemDto>> response = restTemplate.exchange(
                API_URL + "/items",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<MenuItemDto>>() {}
            );
            
            List<MenuItemDto> items = response.getBody();
            model.addAttribute("items", items);
            model.addAttribute("title", "Thực đơn - Restaurant Manager");
            model.addAttribute("activeTab", "food");
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("items", new ArrayList<>());
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
} 