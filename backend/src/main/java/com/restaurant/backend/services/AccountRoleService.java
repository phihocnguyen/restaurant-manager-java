package com.restaurant.backend.services;

import com.restaurant.backend.domains.dto.AccountRole.AccountRoleDto;
import com.restaurant.backend.domains.entities.AccountRole;

import java.util.List;
import java.util.Optional;

public interface AccountRoleService {
    public AccountRole save(AccountRole accountRole);
    public Optional<AccountRole> findById(int id);
    public List<AccountRoleDto> findAll();
}
