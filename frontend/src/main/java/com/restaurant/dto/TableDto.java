package com.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableDto {
    private Integer id;
    private Integer tabNum;
    private Boolean tabStatus;
    private Boolean isdeleted;
} 