package com.restaurant.backend.domains.dto.ReceiptDetail.dto;

import com.restaurant.backend.domains.entities.MenuItem;
import com.restaurant.backend.domains.entities.Receipt;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateReceiptDetailDto {
    @NotNull
    private Integer itemId;
    
    @NotNull
    private Integer quantity;
    
    private BigDecimal price;
    
    private Integer receiptId;
}
