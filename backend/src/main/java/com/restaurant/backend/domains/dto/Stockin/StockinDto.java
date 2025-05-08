package com.restaurant.backend.domains.dto.Stockin;

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
public class StockinDto {
    private Integer id;

    private Instant stoDate;

    private BigDecimal stoPrice;
}
