package com.restaurant.backend.controllers;

import com.restaurant.backend.domains.dto.AccountRole.AccountRoleDto;
import com.restaurant.backend.domains.dto.AccountRole.dto.CreateAccountRoleDto;
import com.restaurant.backend.domains.entities.AccountRole;
import com.restaurant.backend.mappers.impl.AccountRoleMapper;
import com.restaurant.backend.services.AccountRoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountRoleController {
    private final AccountRoleService accountRoleService;
    private final AccountRoleMapper accountRoleMapper;
    public AccountRoleController(AccountRoleService accountRoleService, AccountRoleMapper accountRoleMapper) {
        this.accountRoleService = accountRoleService;
        this.accountRoleMapper = accountRoleMapper;
    }
    @PostMapping(path="/account-role")
    public ResponseEntity<AccountRoleDto> create(@RequestBody CreateAccountRoleDto createAccountRoleDto) {
        AccountRole accountRole = this.accountRoleMapper.mapTo(createAccountRoleDto);
        return new ResponseEntity<>(this.accountRoleMapper.mapFrom(this.accountRoleService.save(accountRole)), HttpStatus.OK);
    }
}
