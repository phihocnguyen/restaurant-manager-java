package com.restaurant.backend.domains.dto.ReceiptDetail.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateManyReceiptDetailsDto {
    private List<CreateReceiptDetailDto> details;
}
