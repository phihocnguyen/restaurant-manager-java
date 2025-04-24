package com.restaurant.backend.controllers;

import com.restaurant.backend.domains.dto.Account.AccountDto;
import com.restaurant.backend.domains.dto.Account.dto.ForgotPasswordDto;
import com.restaurant.backend.domains.dto.Account.dto.UpdateAccountDto;
import com.restaurant.backend.domains.dto.Account.dto.VerifyDto;
import com.restaurant.backend.domains.entities.Account;
import com.restaurant.backend.domains.dto.Account.dto.LoginDto;
import com.restaurant.backend.mappers.impl.AccountMapper;
import com.restaurant.backend.services.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<AccountDto> login(@RequestBody LoginDto loginDto) {
        AccountDto dbAccountDto = this.accountMapper.mapFrom(this.accountService.checkLogin(loginDto));
        return this.accountService.checkLogin(loginDto) != null ? new ResponseEntity<>(dbAccountDto,HttpStatus.OK) : new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
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

    @PutMapping(path="/update-account/{username}")
    public ResponseEntity<AccountDto> updateAccount(@RequestBody UpdateAccountDto updateAccountDto, @PathVariable String username) {
        Account account = accountMapper.mapTo(updateAccountDto);
        account.setAccUsername(username);
        Account savedAccount = this.accountService.save(account);
        return new ResponseEntity<>(accountMapper.mapFrom(savedAccount), HttpStatus.OK);
    }

    @PatchMapping(path="/update-account/{username}")
    public ResponseEntity<AccountDto> partialUpdateAccount(@RequestBody UpdateAccountDto updateAccountDto, @PathVariable String username) {
        Account account = accountMapper.mapTo(updateAccountDto);
        Account dbAccount = this.accountService.getAccountByUsername(username);

        if (account.getAccEmail() != null) {
            dbAccount.setAccEmail(account.getAccEmail());
        }
        if (account.getAccPhone() != null) {
            dbAccount.setAccPhone(account.getAccPhone());
        }
        if (account.getAccGender() != null) {
            dbAccount.setAccGender(account.getAccGender());
        }
        if (account.getAccBday() != null) {
            dbAccount.setAccBday(account.getAccBday());
        }
        if (account.getAccAddress() != null) {
            dbAccount.setAccAddress(account.getAccAddress());
        }
        if (account.getAccPassword() != null) {
            dbAccount.setAccPassword(account.getAccPassword());
        }
        if (account.getRole() != null) {
            dbAccount.setRole(account.getRole());
        }
        if(account.getAccDisplayname() != null) {
            dbAccount.setAccDisplayname(account.getAccDisplayname());
        }

        Account savedAccount = this.accountService.save(dbAccount);
        return new ResponseEntity<>(accountMapper.mapFrom(savedAccount), HttpStatus.OK);
    }
}
