package com.restaurant.manager.controller;

import com.restaurant.dto.MenuItemDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Controller
public class PaymentController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${backend.api.url}")
    private String backendApiUrl;

    @GetMapping("/payment")
    public String paymentPage(@RequestParam String items, Model model) {
        // Parse the items parameter (format: "id1-qty1,id2-qty2,...")
        List<Map<String, Object>> selectedItems = new ArrayList<>();
        String[] itemPairs = items.split(",");
        
        for (String pair : itemPairs) {
            String[] parts = pair.split("-");
            if (parts.length == 2) {
                String itemId = parts[0];
                int quantity = Integer.parseInt(parts[1]);
                
                // Fetch item details from backend
                try {
                    MenuItemDto item = restTemplate.getForObject(
                        backendApiUrl + "/items/" + itemId,
                        MenuItemDto.class
                    );
                    
                    if (item != null) {
                        selectedItems.add(Map.of(
                            "id", item.getId(),
                            "name", item.getItemName(),
                            "price", item.getItemSprice(),
                            "quantity", quantity,
                            "total", item.getItemSprice() * quantity
                        ));
                    }
                } catch (Exception e) {
                    // Log error and continue with other items
                    System.err.println("Error fetching item " + itemId + ": " + e.getMessage());
                }
            }
        }
        
        // Calculate total amount
        double totalAmount = selectedItems.stream()
            .mapToDouble(item -> (Double) item.get("total"))
            .sum();
        
        model.addAttribute("selectedItems", selectedItems);
        model.addAttribute("totalAmount", totalAmount);
        
        return "payment";
    }

    @PostMapping("/payment/submit")
    @ResponseBody
    public ResponseEntity<?> submitOrder(@RequestBody Map<String, Object> orderData) {
        try {
            // Prepare the order data for the backend
            Map<String, Object> orderRequest = new HashMap<>();
            orderRequest.put("customerName", orderData.get("fullName"));
            orderRequest.put("phone", orderData.get("phone"));
            orderRequest.put("address", orderData.get("address"));
            orderRequest.put("paymentMethod", orderData.get("paymentMethod"));
            orderRequest.put("totalAmount", orderData.get("totalAmount"));
            
            // Parse items string back into a list of order items
            String itemsStr = (String) orderData.get("items");
            List<Map<String, Object>> orderItems = new ArrayList<>();
            String[] itemPairs = itemsStr.split(",");
            
            for (String pair : itemPairs) {
                String[] parts = pair.split("-");
                if (parts.length == 2) {
                    Map<String, Object> orderItem = new HashMap<>();
                    orderItem.put("itemId", parts[0]);
                    orderItem.put("quantity", Integer.parseInt(parts[1]));
                    orderItems.add(orderItem);
                }
            }
            orderRequest.put("items", orderItems);

            // Send order to backend
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(orderRequest, headers);
            
            ResponseEntity<?> response = restTemplate.postForEntity(
                backendApiUrl + "/orders",
                request,
                Object.class
            );

            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error processing order: " + e.getMessage());
        }
    }
} 