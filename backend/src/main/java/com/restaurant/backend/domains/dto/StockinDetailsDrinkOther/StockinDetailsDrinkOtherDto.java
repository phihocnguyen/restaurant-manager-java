package com.restaurant.backend.domains.dto.StockinDetailsDrinkOther;

import com.restaurant.backend.domains.dto.MenuItem.MenuItemDto;
import com.restaurant.backend.domains.dto.Stockin.StockinDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockinDetailsDrinkOtherDto {
    private StockinDto sto;

    private MenuItemDto item;

    private Integer quantityUnits;

    private BigDecimal cprice;

    private BigDecimal totalCprice;
}
