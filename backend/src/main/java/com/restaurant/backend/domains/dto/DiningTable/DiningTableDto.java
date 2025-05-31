package com.restaurant.backend.domains.dto.DiningTable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.restaurant.backend.domains.dto.DiningTable.enums.TableStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiningTableDto {
    private Integer id;

    private Short tabNum;

    private TableStatus tabStatus = TableStatus.EMPTY;

    private Boolean isdeleted = false;
}
