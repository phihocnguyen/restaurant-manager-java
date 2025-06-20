package com.restaurant.backend.domains.dto.Account;

import com.restaurant.backend.domains.dto.AccountRole.AccountRoleDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 100, message = "Username must be between 3 and 100 characters")
    private String accUsername;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String accPassword;

    @NotBlank(message = "Email is required")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String accEmail;

    @Size(max = 100, message = "Display name must not exceed 100 characters")
    private String accDisplayname = "User";

    private Boolean accGender = false;

    private LocalDate accBday = LocalDate.now();

    @Size(max = 100, message = "Address must not exceed 100 characters")
    private String accAddress = "";

    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String accPhone;

    private AccountRoleDto role;

    private Boolean isdeleted = false;

    private String cccd;

    private Boolean verified;
}
