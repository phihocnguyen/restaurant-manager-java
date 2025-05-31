package com.restaurant.backend.services;

import com.restaurant.backend.domains.dto.OrderOnline.dto.OrderOnlineDTO;
import com.restaurant.backend.domains.dto.OrderOnline.dto.OrderOnlineDetailsDTO;

import java.util.List;
import java.util.Optional;

public interface OrderOnlineService {
    /**
     * Create a new online order with its details
     * @param orderOnlineDTO the order DTO containing order details
     * @return the created order DTO
     */
    OrderOnlineDTO createOrder(OrderOnlineDTO orderOnlineDTO);

    /**
     * Get all orders
     * @return list of all order DTOs
     */
    List<OrderOnlineDTO> getAllOrders();

    /**
     * Get order by ID
     * @param id the order ID
     * @return the order DTO if found
     */
    Optional<OrderOnlineDTO> getOrderById(Long id);

    /**
     * Get orders by user ID
     * @param userId the user ID
     * @return list of order DTOs for the user
     */
    List<OrderOnlineDTO> getOrdersByUserId(String userId);

    /**
     * Get orders by status
     * @param status the order status
     * @return list of order DTOs with the specified status
     */
    List<OrderOnlineDTO> getOrdersByStatus(String status);

    /**
     * Update order status
     * @param orderId the order ID
     * @param newStatus the new status
     * @return the updated order DTO
     */
    OrderOnlineDTO updateOrderStatus(Long orderId, String newStatus);

    /**
     * Cancel an order
     * @param orderId the order ID
     */
    void cancelOrder(Long orderId);

    /**
     * Get order details by order ID
     * @param orderId the order ID
     * @return list of order details DTOs for the order
     */
    List<OrderOnlineDetailsDTO> getOrderDetailsByOrderId(Long orderId);
} 