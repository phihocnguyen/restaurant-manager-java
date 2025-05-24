package com.restaurant.backend.domains.dto.Account;

import com.restaurant.backend.domains.dto.AccountRole.AccountRoleDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDto {
    private String accUsername;

    private String accPassword;

    private String accEmail;

    private String accDisplayname = "User";

    private Boolean accGender = false;

    private LocalDate accBday = LocalDate.now();

    private String accAddress = "";

    private String accPhone;

    private AccountRoleDto role;

    private Boolean isdeleted = false;
}
