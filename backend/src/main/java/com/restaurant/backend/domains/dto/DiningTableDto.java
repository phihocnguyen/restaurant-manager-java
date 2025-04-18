package com.restaurant.backend.domains.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiningTableDto {
    private Integer id;

    private Short tabNum;

    private Boolean tabStatus = false;

    private Boolean isdeleted = false;
}
