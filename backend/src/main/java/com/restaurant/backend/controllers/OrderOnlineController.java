package com.restaurant.backend.controllers;

import com.restaurant.backend.domains.dto.OrderOnline.dto.OrderOnlineDTO;
import com.restaurant.backend.domains.dto.OrderOnline.enums.OrderStatus;
import com.restaurant.backend.services.OrderOnlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderOnlineController {

    @Autowired
    private OrderOnlineService orderOnlineService;

    @PostMapping
    public ResponseEntity<OrderOnlineDTO> createOrder(@RequestBody OrderOnlineDTO orderOnline) {
        return ResponseEntity.ok(orderOnlineService.createOrder(orderOnline));
    }

    @GetMapping
    public ResponseEntity<List<OrderOnlineDTO>> getAllOrders() {
        return ResponseEntity.ok(orderOnlineService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderOnlineDTO> getOrderById(@PathVariable Long id) {
        return orderOnlineService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderOnlineDTO>> getOrdersByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(orderOnlineService.getOrdersByUserId(userId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderOnlineDTO>> getOrdersByStatus(@PathVariable OrderStatus status) {
        return ResponseEntity.ok(orderOnlineService.getOrdersByStatus(status.toString()));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderOnlineDTO> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String newStatus) {
        return ResponseEntity.ok(orderOnlineService.updateOrderStatus(id, newStatus));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
        orderOnlineService.cancelOrder(id);
        return ResponseEntity.ok().build();
    }
} 