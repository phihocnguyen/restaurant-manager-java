package com.restaurant.backend.controllers;

import com.restaurant.backend.domains.dto.OrderOnline.dto.OrderOnlineDTO;
import com.restaurant.backend.domains.dto.OrderOnline.enums.OrderStatus;
import com.restaurant.backend.services.OrderOnlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderOnlineController {

    @Autowired
    private OrderOnlineService orderOnlineService;

    @Autowired
    private Cloudinary cloudinary;

    @PostMapping
    public ResponseEntity<OrderOnlineDTO> createOrder(@RequestBody OrderOnlineDTO orderOnline) {
        return ResponseEntity.ok(orderOnlineService.createOrder(orderOnline));
    }

    @GetMapping
    public ResponseEntity<List<OrderOnlineDTO>> getAllOrders() {
        List<OrderOnlineDTO> orders = orderOnlineService.getAllOrders();
        // Load order details for each order
        orders.forEach(order -> order.setOrderDetails(orderOnlineService.getOrderDetailsByOrderId(order.getId())));
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderOnlineDTO> getOrderById(@PathVariable Long id) {
        return orderOnlineService.getOrderById(id)
                .map(order -> {
                    // Load order details for the order
                    order.setOrderDetails(orderOnlineService.getOrderDetailsByOrderId(order.getId()));
                    return ResponseEntity.ok(order);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderOnlineDTO>> getOrdersByUserId(@PathVariable String userId) {
        List<OrderOnlineDTO> orders = orderOnlineService.getOrdersByUserId(userId);
        // Load order details for each order
        orders.forEach(order -> order.setOrderDetails(orderOnlineService.getOrderDetailsByOrderId(order.getId())));
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderOnlineDTO>> getOrdersByStatus(@PathVariable OrderStatus status) {
        List<OrderOnlineDTO> orders = orderOnlineService.getOrdersByStatus(status.toString());
        // Load order details for each order
        orders.forEach(order -> order.setOrderDetails(orderOnlineService.getOrderDetailsByOrderId(order.getId())));
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderOnlineDTO> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String newStatus,
            @RequestParam(required = false) Integer employeeId) {
        OrderOnlineDTO order = orderOnlineService.updateOrderStatus(id, newStatus, employeeId);
        // Load order details for the updated order
        order.setOrderDetails(orderOnlineService.getOrderDetailsByOrderId(order.getId()));
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderOnlineDTO> updateOrder(
            @PathVariable Long id,
            @RequestBody OrderOnlineDTO updatedOrderDTO) {
        OrderOnlineDTO updatedOrder = orderOnlineService.updateOrder(id, updatedOrderDTO);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
        orderOnlineService.cancelOrder(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/payment-proof")
    public ResponseEntity<OrderOnlineDTO> uploadPaymentProof(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        try {
            // Upload file to Cloudinary
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) uploadResult.get("secure_url");

            // Update order with payment image URL
            OrderOnlineDTO updatedOrder = orderOnlineService.updatePaymentProof(id, imageUrl);
            return ResponseEntity.ok(updatedOrder);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
} 