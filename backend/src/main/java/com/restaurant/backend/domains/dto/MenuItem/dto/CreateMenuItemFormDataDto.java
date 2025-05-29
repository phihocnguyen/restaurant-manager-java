package com.restaurant.backend.domains.dto.MenuItem.dto;

import com.restaurant.backend.domains.dto.MenuItem.interfaces.ItemType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateMenuItemFormDataDto {
    @NotNull
    private ItemType itemType;

    @Size(max = 100)
    @NotNull
    private String itemName;

    private BigDecimal itemCprice = BigDecimal.ZERO;

    private BigDecimal itemSprice = BigDecimal.ZERO;

    private Double instock = 0.0;

    private Boolean isdeleted = false;

    private MultipartFile image;
} 