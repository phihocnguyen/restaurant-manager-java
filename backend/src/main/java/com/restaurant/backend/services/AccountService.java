package com.restaurant.backend.services;

import com.restaurant.backend.domains.dto.Account.AccountDto;
import com.restaurant.backend.domains.dto.Account.dto.ForgotPasswordDto;
import com.restaurant.backend.domains.dto.Account.dto.UpdateAccountDto;
import com.restaurant.backend.domains.entities.Account;
import com.restaurant.backend.domains.dto.Account.dto.LoginDto;

public interface AccountService {
    public AccountDto signup(AccountDto accountDto);
    public AccountDto login(LoginDto loginDto);
    public boolean forgotPassword(ForgotPasswordDto forgotPasswordDto);

    boolean verify(String email, String code);

    public AccountDto updateAccount(UpdateAccountDto updateAccountDto, String username);
    public AccountDto partialUpdateAccount(UpdateAccountDto updateAccountDto, String username);


}
