package com.restaurant.backend.controllers;

import com.restaurant.backend.domains.dto.Account.AccountDto;
import com.restaurant.backend.domains.dto.Account.dto.ForgotPasswordDto;
import com.restaurant.backend.domains.dto.Account.dto.UpdateAccountDto;
import com.restaurant.backend.domains.dto.Account.dto.VerifyDto;
import com.restaurant.backend.domains.dto.Account.dto.LoginDto;
import com.restaurant.backend.services.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Random;

@RestController
public class AccountController {
    private final AccountService accountService;
    @Autowired
    private com.restaurant.backend.other_services.EmailService emailService;
    @Autowired
    private com.restaurant.backend.repositories.AccountRepository accountRepository;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(path = "/signup")
    public ResponseEntity<AccountDto> signup(@RequestBody AccountDto accountDto) {
        AccountDto savedDto = accountService.signup(accountDto);
        return new ResponseEntity<>(savedDto, HttpStatus.OK);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<AccountDto> login(@RequestBody LoginDto loginDto) {
        AccountDto dbAccountDto = accountService.login(loginDto);
        return dbAccountDto != null ? new ResponseEntity<>(dbAccountDto, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(path = "/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordDto forgotPasswordDto) {
        boolean success = accountService.forgotPassword(forgotPasswordDto);
        return success ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(path = "/verify")
    public boolean verify(@RequestBody VerifyDto verifyDto) {
        return accountService.verify(verifyDto.getAccEmail(), verifyDto.getCode());
    }

    @PutMapping(path = "/update-account/{username}")
    public ResponseEntity<AccountDto> updateAccount(@RequestBody UpdateAccountDto updateAccountDto, @PathVariable String username) {
        AccountDto updated = accountService.updateAccount(updateAccountDto, username);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @PatchMapping(path = "/update-account/{username}")
    public ResponseEntity<AccountDto> partialUpdateAccount(@RequestBody UpdateAccountDto updateAccountDto, @PathVariable String username) {
        AccountDto updated = accountService.partialUpdateAccount(updateAccountDto, username);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @PostMapping(path = "/send-verification")
    public ResponseEntity<String> sendVerification(@RequestParam String email) {
        // Kiểm tra user có tồn tại không
        var accountOpt = accountRepository.findOneByAccEmail(email);
        if (accountOpt.isEmpty()) {
            return new ResponseEntity<>("Email không tồn tại trong hệ thống", HttpStatus.NOT_FOUND);
        }
        // Sinh mã token
        String code = String.valueOf(100000 + new Random().nextInt(899999));
        // Gửi mail
        emailService.sendVerificationCode(email, code);
        // (Có thể lưu code vào DB hoặc cache nếu cần xác thực sau này)
        return new ResponseEntity<>("Đã gửi mã xác nhận đến email", HttpStatus.OK);
    }
}
