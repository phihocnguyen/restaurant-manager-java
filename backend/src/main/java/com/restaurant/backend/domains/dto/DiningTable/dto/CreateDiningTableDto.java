package com.restaurant.backend.domains.dto.DiningTable.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDiningTableDto {
    private Short tabNum;

    private Boolean tabStatus = false;

    private Boolean isdeleted = false;
}
