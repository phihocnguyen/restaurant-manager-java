package com.restaurant.backend.domains.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDto {
    private Integer id;

    private String cusName;

    private String cusAddr;

    private String cusPhone;

    private String cusCccd;

    private String cusEmail;

    private Boolean isvip = false;

    private Boolean isdeleted = false;
}
