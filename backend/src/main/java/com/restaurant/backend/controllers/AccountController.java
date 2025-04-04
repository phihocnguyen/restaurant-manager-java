package com.restaurant.backend.controllers;

import com.restaurant.backend.services.AccountService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {
    private final AccountService accountService;
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }
}
