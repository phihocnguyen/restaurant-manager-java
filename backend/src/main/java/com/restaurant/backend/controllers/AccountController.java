package com.restaurant.backend.controllers;

import com.restaurant.backend.domains.dto.Account.AccountDto;
import com.restaurant.backend.domains.dto.Account.dto.ForgotPasswordDto;
import com.restaurant.backend.domains.dto.Account.dto.VerifyDto;
import com.restaurant.backend.domains.entities.Account;
import com.restaurant.backend.domains.dto.Account.dto.LoginDto;
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

    @PostMapping(path = "/signup") // may not use it ever
    public ResponseEntity<AccountDto> login(@RequestBody AccountDto accountDto) {
        Account account = accountMapper.mapTo(accountDto);
        Account savedAccount = this.accountService.save(account);
        return new ResponseEntity<>(accountMapper.mapFrom(savedAccount), HttpStatus.OK); // not showing body properly
    }

    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        return this.accountService.checkLogin(loginDto) ? new ResponseEntity<>(loginDto.getAccUsername(),HttpStatus.OK) : new ResponseEntity<>("Please check your credientials and try again",HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(path= "/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordDto forgotPasswordDto) {
        try {
            Account foundAccount = this.accountService.getAccountByUsername(forgotPasswordDto.getAccUsername());
            if(foundAccount != null) {
                String sentCode = this.accountService.sendVerificationCode(foundAccount.getAccEmail());
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        }
        catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @PostMapping(path="/verify")
    public boolean verify(@RequestBody VerifyDto verifyDto) {
        return this.accountService.verify(verifyDto.getAccEmail(), verifyDto.getCode());
    }

}
