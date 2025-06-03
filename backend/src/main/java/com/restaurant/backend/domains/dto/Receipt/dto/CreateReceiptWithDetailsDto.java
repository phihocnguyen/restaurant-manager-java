package com.restaurant.backend.domains.dto.Receipt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateReceiptWithDetailsDto {
    private CreateReceiptDto receipt;
    private ReceiptDetailsWrapper details;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReceiptDetailsWrapper {
        private List<ReceiptDetailItem> details;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReceiptDetailItem {
        private Integer itemId;
        private Integer quantity;
    }
} 