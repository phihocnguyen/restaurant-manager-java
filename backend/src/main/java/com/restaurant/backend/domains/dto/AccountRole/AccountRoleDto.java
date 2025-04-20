package com.restaurant.backend.domains.dto.AccountRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountRoleDto {
    private Integer id;

    private String roleName;
}
