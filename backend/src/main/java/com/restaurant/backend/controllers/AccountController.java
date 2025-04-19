package com.restaurant.backend.controllers;

import com.restaurant.backend.domains.dto.Account.AccountDto;
import com.restaurant.backend.domains.entities.Account;
import com.restaurant.backend.dto.LoginDto;
import com.restaurant.backend.mappers.impl.AccountMapper;
import com.restaurant.backend.services.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {
    private final AccountService accountService;
    private final AccountMapper accountMapper;
    public AccountController(AccountService accountService, AccountMapper accountMapper) {
        this.accountService = accountService;
        this.accountMapper = accountMapper;
    }

    @PostMapping(path = "/auth/signup") // may not use it ever
    public ResponseEntity<AccountDto> login(@RequestBody AccountDto accountDto) {
        Account account = accountMapper.mapTo(accountDto);
        Account savedAccount = this.accountService.save(account);
        return new ResponseEntity<>(accountMapper.mapFrom(savedAccount), HttpStatus.OK); // not returning body properly yet
    }

    @PostMapping(path = "/auth/login")
    public ResponseEntity<AccountDto> login(@RequestBody LoginDto loginDto) {
        return this.accountService.checkLogin(loginDto) ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
