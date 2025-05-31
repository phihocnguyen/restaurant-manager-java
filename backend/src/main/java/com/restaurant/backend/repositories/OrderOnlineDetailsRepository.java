package com.restaurant.backend.repositories;

import com.restaurant.backend.domains.dto.OrderOnline.OrderOnlineDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderOnlineDetailsRepository extends JpaRepository<OrderOnlineDetails, Long> {
    List<OrderOnlineDetails> findByOrderOnlineId(Long orderId);
} 