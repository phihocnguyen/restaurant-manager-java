package com.restaurant.backend.domains.dto.OrderOnline.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderOnlineDetailsDTO {
    private Long id;
    private Long orderOnlineId;
    private Long itemId;
    private Integer quantity;
    private BigDecimal price;
    private String note;
} 