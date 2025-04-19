package com.restaurant.backend.domains.dto.Customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
