package com.restaurant.backend.domains.dto.Stockin;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockInDetailsDrinkOtherDTO {
    @JsonProperty("stockInId")
    private Integer stockInId;

    @NotNull(message = "itemId is required")
    @JsonProperty("itemId")
    private Integer itemId;

    @NotNull(message = "quantityUnits is required")
    @JsonProperty("quantityUnits")
    private Integer quantityUnits;

    @NotNull(message = "cPrice is required")
    @JsonProperty("cPrice")
    private BigDecimal cPrice;

    @JsonProperty("totalCPrice")
    private BigDecimal totalCPrice;
}