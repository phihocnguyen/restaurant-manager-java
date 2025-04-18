package com.restaurant.backend.domains.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReceiptDetailDto {
    private ReceiptDto rec;

    private MenuItemDto item;

    private Integer quantity;

    private BigDecimal price;
}
