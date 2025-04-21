package com.restaurant.backend.services;

import com.restaurant.backend.domains.entities.AccountRole;

import java.util.Optional;

public interface AccountRoleService {
    public AccountRole save(AccountRole accountRole);
    public Optional<AccountRole> findById(int id);
}
