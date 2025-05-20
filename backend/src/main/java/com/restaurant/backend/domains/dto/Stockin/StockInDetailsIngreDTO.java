package com.restaurant.backend.domains.dto.Stockin;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockInDetailsIngreDTO {
    @JsonProperty("stockInId")
    private Integer stockInId;

    @NotNull(message = "ingredientId is required")
    @JsonProperty("ingredientId")
    private Integer ingredientId;

    @NotNull(message = "quantityKg is required")
    @JsonProperty("quantityKg")
    private Double quantityKg;

    @NotNull(message = "cPrice is required")
    @JsonProperty("cPrice")
    private Double cPrice;

    @JsonProperty("totalCPrice")
    private Double totalCPrice;
}