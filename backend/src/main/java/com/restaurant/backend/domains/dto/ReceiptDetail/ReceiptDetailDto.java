package com.restaurant.backend.domains.dto.ReceiptDetail;

import com.restaurant.backend.domains.dto.MenuItem.MenuItemDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReceiptDetailDto {
    private Integer receipt;
    private MenuItemDto menuItem;
    private Integer quantity;
    private BigDecimal price = BigDecimal.ZERO;
}
