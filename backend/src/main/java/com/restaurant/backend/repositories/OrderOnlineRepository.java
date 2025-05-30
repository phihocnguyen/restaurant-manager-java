package com.restaurant.backend.repositories;

import com.restaurant.backend.domains.dto.OrderOnline.OrderOnline;
import com.restaurant.backend.domains.dto.OrderOnline.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderOnlineRepository extends JpaRepository<OrderOnline, Long> {
    List<OrderOnline> findByUserId(String userId);
    List<OrderOnline> findByStatus(OrderStatus status);
} 