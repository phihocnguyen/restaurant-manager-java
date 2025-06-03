package com.restaurant.backend.domains.dto.ReceiptDetail.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReceiptDetailItem {
    private Integer itemId;
    private Integer quantity;
} 