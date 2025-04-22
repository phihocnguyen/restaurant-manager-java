package com.restaurant.backend.domains.dto.ReceiptDetail.dto;

import com.restaurant.backend.domains.dto.Receipt.dto.CreateReceiptDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateReceiptWithOneReceiptDetail {
    private CreateReceiptDetailDto detail;
    private CreateReceiptDto receipt;
}
