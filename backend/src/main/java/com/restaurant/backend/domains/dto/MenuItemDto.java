package com.restaurant.backend.domains.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

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
