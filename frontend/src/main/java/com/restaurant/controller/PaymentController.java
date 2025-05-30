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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${backend.api.url:http://localhost:8080}")
    private String backendApiUrl;

    @GetMapping
    public String paymentPage(@RequestParam String items, Model model) {
        List<Map<String, Object>> selectedItems = new ArrayList<>();
        String[] itemPairs = items.split(",");

        for (String pair : itemPairs) {
            String[] parts = pair.split("-");
            if (parts.length == 2) {
                String itemId = parts[0];
                int quantity = Integer.parseInt(parts[1]);

                try {
                    // Call API to get single item details
                    MenuItemDto item = restTemplate.getForObject(
                            backendApiUrl + "/items/" + itemId,
                            MenuItemDto.class
                    );

                    if (item != null) {
                        logger.info("Successfully fetched item details for ID {}: {}", itemId, item);
                        // Add item details and quantity to the list
                        Map<String, Object> selectedItem = new HashMap<>();
                        selectedItem.put("id", item.getId());
                        selectedItem.put("name", item.getItemName()); // Use itemName from DTO
                        selectedItem.put("price", item.getItemSprice()); // Use itemSprice from DTO
                        selectedItem.put("quantity", quantity);
                        selectedItem.put("total", item.getItemSprice() * quantity);
                        selectedItem.put("imageUrl", item.getItemImg()); // Use itemImg from DTO

                        selectedItems.add(selectedItem);
                    } else {
                        // Handle case where item is not found
                        System.err.println("Item with ID " + itemId + " not found.");
                    }
                } catch (Exception e) {
                    // Handle API call errors
                    System.err.println("Error fetching item " + itemId + ": " + e.getMessage());
                    // Optionally add an error message to the model
                    model.addAttribute("error", "Failed to load item details.");
                }
            }
        }

        // Calculate total amount
        double totalAmount = selectedItems.stream()
                .mapToDouble(item -> (Double) item.get("total"))
                .sum();

        model.addAttribute("selectedItems", selectedItems);
        model.addAttribute("totalAmount", totalAmount);

        return "payment"; // This should match the name of your Thymeleaf template (payment.html)
    }

    @PostMapping("/submit")
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