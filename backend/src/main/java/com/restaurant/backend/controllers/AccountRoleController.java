package com.restaurant.backend.controllers;

import com.restaurant.backend.services.AccountRoleService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountRoleController {
    private final AccountRoleService accountRoleService;
    public AccountRoleController(AccountRoleService accountRoleService) {
        this.accountRoleService = accountRoleService;
    }
}
