package com.restaurant.backend.domains.dto.MenuItem;

import com.restaurant.backend.domains.dto.MenuItem.interfaces.ItemType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotNull
    private ItemType itemType;

    @Size(max = 100)
    @NotNull
    private String itemName;

    private String itemImg;

    private BigDecimal itemCprice = BigDecimal.ZERO;

    private BigDecimal itemSprice = BigDecimal.ZERO;

    private Double instock = 0.0;

    private Boolean isdeleted = false;
}
