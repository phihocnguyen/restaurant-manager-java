package com.restaurant.backend.services.impl;

import com.restaurant.backend.repositories.AccountRoleRepository;
import com.restaurant.backend.services.AccountRoleService;
import org.springframework.stereotype.Service;

@Service
public class AccountRoleServiceImpl implements AccountRoleService {
    private final AccountRoleRepository accountRoleRepository;
    public AccountRoleServiceImpl(AccountRoleRepository accountRoleRepository) {
        this.accountRoleRepository = accountRoleRepository;
    }
}
