package com.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountResponse {
    private String accUsername;
    private String accPassword;
    private String accEmail;
    private String accDisplayname;
    private Boolean accGender;
    private LocalDate accBday;
    private String accAddress;
    private String accPhone;
    private AccountRoleResponse role;
    private Boolean isdeleted;
} 