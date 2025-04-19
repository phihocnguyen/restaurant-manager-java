package com.restaurant.backend.domains.dto.MenuItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuItemDto {
    private Integer id;

    private String itemType;

    private String itemName;

    private String itemImg;

    private BigDecimal itemCprice;

    private Double instock;

    private Boolean isdeleted = false;
}
