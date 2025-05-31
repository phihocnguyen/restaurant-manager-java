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
            @RequestParam String newStatus) {
        OrderOnlineDTO order = orderOnlineService.updateOrderStatus(id, newStatus);
        // Load order details for the updated order
        order.setOrderDetails(orderOnlineService.getOrderDetailsByOrderId(order.getId()));
        return ResponseEntity.ok(order);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
        orderOnlineService.cancelOrder(id);
        return ResponseEntity.ok().build();
    }
} 