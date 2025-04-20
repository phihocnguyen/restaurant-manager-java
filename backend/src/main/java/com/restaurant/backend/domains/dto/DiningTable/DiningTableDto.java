package com.restaurant.backend.domains.dto.DiningTable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
