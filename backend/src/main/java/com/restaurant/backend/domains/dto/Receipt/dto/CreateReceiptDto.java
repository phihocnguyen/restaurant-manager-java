package com.restaurant.backend.domains.dto.Receipt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateReceiptDto {
    private Integer empId;
    private Integer cusId;
    private Integer tabId;
    private Instant recTime;
    private Boolean isdeleted = Boolean.FALSE;
}
