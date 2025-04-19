package com.restaurant.backend.services;

import com.restaurant.backend.domains.entities.Account;
import com.restaurant.backend.dto.LoginDto;

public interface AccountService {
    public Account save(Account account);
    public boolean checkLogin(LoginDto loginDto);
}
