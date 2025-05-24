package com.restaurant.backend.services.impl;

import com.restaurant.backend.domains.dto.AccountRole.AccountRoleDto;
import com.restaurant.backend.domains.entities.AccountRole;
import com.restaurant.backend.repositories.AccountRoleRepository;
import com.restaurant.backend.services.AccountRoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountRoleServiceImpl implements AccountRoleService {
    private final AccountRoleRepository accountRoleRepository;
    public AccountRoleServiceImpl(AccountRoleRepository accountRoleRepository) {
        this.accountRoleRepository = accountRoleRepository;
    }
    public AccountRole save(AccountRole accountRole) {
        return this.accountRoleRepository.save(accountRole);
    }
    public Optional<AccountRole> findById(int id) {
        return this.accountRoleRepository.findById(id);
    }
    public List<AccountRoleDto> findAll() {
        return this.accountRoleRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private AccountRoleDto convertToDto(AccountRole accountRole) {
        AccountRoleDto accountRoleDto = new AccountRoleDto();
        accountRoleDto.setId(accountRole.getId());
        accountRoleDto.setRoleName(accountRole.getRoleName());
        return accountRoleDto;
    }
}
