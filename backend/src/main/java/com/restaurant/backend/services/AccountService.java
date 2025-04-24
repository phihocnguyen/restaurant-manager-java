package com.restaurant.backend.services;

import com.restaurant.backend.domains.entities.Account;
import com.restaurant.backend.domains.dto.Account.dto.LoginDto;

public interface AccountService {
    public Account save(Account account);
    public Account checkLogin(LoginDto loginDto);
    public Account getAccountByUsername(String username);
    public Account getAccountByEmail(String email);
    public String sendVerificationCode(String email);
    public boolean verify(String email, String code);

}
