package com.restaurant.backend.domains.dto.Customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    private Integer id;
    private String name;
    private String address;
    private String phone;
    private String cccd;
    private String email;
    private Boolean isvip;
    private Boolean isdeleted;
}
