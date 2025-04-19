package com.restaurant.backend.services.impl;

import com.restaurant.backend.domains.entities.Account;
import com.restaurant.backend.domains.entities.AccountRole;
import com.restaurant.backend.dto.LoginDto;
import com.restaurant.backend.repositories.AccountRepository;
import com.restaurant.backend.repositories.AccountRoleRepository;
import com.restaurant.backend.services.AccountService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountRoleRepository accountRoleRepository;
    public AccountServiceImpl(AccountRepository accountRepository, AccountRoleRepository accountRoleRepository) {
        this.accountRepository = accountRepository;
        this.accountRoleRepository = accountRoleRepository;
    }

    public Account save(Account account) {
        AccountRole accountRole = accountRoleRepository.findByRoleName(account.getRole().getRoleName());
        if (accountRole == null) {
            accountRole = new AccountRole();
            accountRole.setRoleName(account.getRole().getRoleName());
            accountRole = this.accountRoleRepository.save(accountRole);
        }
        account.setRole(accountRole);
        return accountRepository.save(account);
    }
    public boolean checkLogin(LoginDto loginDto) {
        Optional<Account> foundAccount = this.accountRepository.findOneByAccUsername(loginDto.getAccUsername());
        if (foundAccount.isPresent()) {
            return foundAccount.get().getAccPassword().equals(loginDto.getAccPassword());
        }
        return false;
    }
}
