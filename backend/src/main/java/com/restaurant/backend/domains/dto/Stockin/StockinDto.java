package com.restaurant.backend.domains.dto.Stockin;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockinDto {
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("date")
    private Instant date;

    @NotNull(message = "price is required")
    @JsonProperty("price")
    private BigDecimal price;

    @Valid
    @JsonProperty("stockInDetailsIngres")
    private List<StockInDetailsIngreDTO> stockInDetailsIngres;

    @Valid
    @JsonProperty("stockInDetailsDrinkOthers")
    private List<StockInDetailsDrinkOtherDTO> stockInDetailsDrinkOthers;
}