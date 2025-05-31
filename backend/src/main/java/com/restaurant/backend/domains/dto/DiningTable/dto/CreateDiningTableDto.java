package com.restaurant.backend.domains.dto.DiningTable.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.restaurant.backend.domains.dto.DiningTable.enums.TableStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDiningTableDto {
    private Short tabNum;

    private TableStatus tabStatus = TableStatus.EMPTY;

    private Boolean isdeleted = false;
}
