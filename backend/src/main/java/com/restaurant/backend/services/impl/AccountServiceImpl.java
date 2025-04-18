package com.restaurant.backend.services.impl;

import com.restaurant.backend.repositories.AccountRepository;
import com.restaurant.backend.services.AccountService;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
}
