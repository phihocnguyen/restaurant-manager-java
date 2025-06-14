package com.restaurant.backend.domains.dto.Receipt.dto;

import com.restaurant.backend.domains.dto.ReceiptDetail.ReceiptDetailDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReceiptHistoryDto {
    private Integer id;
    private Double recPay;
    private String employeeName;
    private String customerName;
    private String paymentMethod;
    private Instant recTime;
    private Boolean isdeleted;
    private Integer tabId;
    private List<ReceiptDetailDto> details;
} 