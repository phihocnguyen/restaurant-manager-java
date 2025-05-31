package com.restaurant.backend.domains.dto.OrderOnline;

import com.restaurant.backend.domains.dto.OrderOnline.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "order_online")
public class OrderOnline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    private String note;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "order_time", nullable = false)
    private Instant orderTime;

    @Column(name = "delivery_time")
    private Instant deliveryTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "payment_method")
    private String paymentMethod;

    @OneToMany(mappedBy = "orderOnline", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderOnlineDetails> orderDetails = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void addOrderDetail(OrderOnlineDetails detail) {
        orderDetails.add(detail);
        detail.setOrderOnline(this);
    }

    public void removeOrderDetail(OrderOnlineDetails detail) {
        orderDetails.remove(detail);
        detail.setOrderOnline(null);
    }
} 