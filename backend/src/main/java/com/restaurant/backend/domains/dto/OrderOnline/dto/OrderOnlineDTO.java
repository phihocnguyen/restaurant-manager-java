package com.restaurant.backend.domains.dto.OrderOnline.dto;

import com.restaurant.backend.domains.dto.OrderOnline.enums.OrderStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.time.Instant;

@Data
public class OrderOnlineDTO {
    private Long id;
    private String userId;  // Optional
    private String customerName;
    private String phoneNumber;
    private String address;
    private String note;
    private BigDecimal totalAmount;
    private Instant orderTime;
    private Instant deliveryTime;
    private OrderStatus status;
    private List<OrderOnlineDetailsDTO> orderDetails;
    private String paymentMethod;
} 