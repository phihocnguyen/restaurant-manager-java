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
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IngredientDto {
    private Integer id;

    private String ingreName;

    private Double instockKg;

    private BigDecimal ingrePrice;

    private Boolean isdeleted = false;
}
