package com.restaurant.backend.services.impl;

import com.restaurant.backend.domains.dto.Account.AccountDto;
import com.restaurant.backend.domains.dto.Account.dto.ForgotPasswordDto;
import com.restaurant.backend.domains.dto.Account.dto.UpdateAccountDto;
import com.restaurant.backend.domains.entities.Account;
import com.restaurant.backend.domains.entities.AccountRole;
import com.restaurant.backend.domains.dto.Account.dto.LoginDto;
import com.restaurant.backend.mappers.impl.AccountMapper;
import com.restaurant.backend.mappers.impl.AccountRoleMapper;
import com.restaurant.backend.other_services.EmailService;
import com.restaurant.backend.repositories.AccountRepository;
import com.restaurant.backend.repositories.AccountRoleRepository;
import com.restaurant.backend.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountRoleRepository accountRoleRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final EmailService emailService;
    private final AccountMapper accountMapper;
    private final AccountRoleMapper accountRoleMapper;

    private final Map<String, String> verificationCodes = new HashMap<>();

    public AccountServiceImpl(
            AccountRepository accountRepository,
            AccountRoleRepository accountRoleRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService,
            AccountMapper accountMapper,
            AccountRoleMapper accountRoleMapper
    ) {
        this.accountRepository = accountRepository;
        this.accountRoleRepository = accountRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.accountMapper = accountMapper;
        this.accountRoleMapper = accountRoleMapper;
    }

    @Override
    public AccountDto signup(AccountDto accountDto) {
        Account account = accountMapper.mapTo(accountDto);
        AccountRole accountRole = accountRoleRepository.findByRoleName(account.getRole().getRoleName());
        if (accountRole == null) {
            accountRole = new AccountRole();
            accountRole.setRoleName(account.getRole().getRoleName());
            accountRole = this.accountRoleRepository.save(accountRole);
        }
        account.setRole(accountRole);
        account.setAccPassword(passwordEncoder.encode(account.getAccPassword()));
        Account saved = accountRepository.save(account);
        return accountMapper.mapFrom(saved);
    }

    @Override
    public AccountDto login(LoginDto loginDto) {
        Optional<Account> found = accountRepository.findOneByAccUsername(loginDto.getAccUsername());
        if (found.isPresent() && passwordEncoder.matches(loginDto.getAccPassword(), found.get().getAccPassword())) {
            return accountMapper.mapFrom(found.get());
        }
        return null;
    }

    @Override
    public boolean forgotPassword(ForgotPasswordDto dto) {
        Optional<Account> found = accountRepository.findOneByAccUsername(dto.getAccUsername());
        if (found.isPresent()) {
            String code = String.valueOf(100000 + new Random().nextInt(899999));
            emailService.sendVerificationCode(found.get().getAccEmail(), code);
            verificationCodes.put(found.get().getAccEmail(), code);
            return true;
        }
        return false;
    }

    @Override
    public boolean verify(String email, String code) {
        return code.equals(verificationCodes.get(email));
    }

    @Override
    public AccountDto updateAccount(UpdateAccountDto updateDto, String username) {
        Account account = accountMapper.mapTo(updateDto);
        account.setAccUsername(username);
        AccountRole role = accountRoleRepository.findByRoleName(account.getRole().getRoleName());
        if (role == null) {
            role = accountRoleRepository.save(account.getRole());
        }
        account.setRole(role);
        account.setAccPassword(passwordEncoder.encode(account.getAccPassword()));
        return accountMapper.mapFrom(accountRepository.save(account));
    }

    @Override
    public AccountDto partialUpdateAccount(UpdateAccountDto updateDto, String username) {
        Account dbAccount = accountRepository.findOneByAccUsername(username).orElseThrow();

        if (updateDto.getAccEmail() != null) dbAccount.setAccEmail(updateDto.getAccEmail());
        if (updateDto.getAccPhone() != null) dbAccount.setAccPhone(updateDto.getAccPhone());
        if (updateDto.getAccGender() != null) dbAccount.setAccGender(updateDto.getAccGender());
        if (updateDto.getAccBday() != null) dbAccount.setAccBday(updateDto.getAccBday());
        if (updateDto.getAccAddress() != null) dbAccount.setAccAddress(updateDto.getAccAddress());
        if (updateDto.getAccPassword() != null) dbAccount.setAccPassword(passwordEncoder.encode(updateDto.getAccPassword()));
        if (updateDto.getRole() != null) {
            AccountRole role = accountRoleRepository.findByRoleName(updateDto.getRole().getRoleName());
            if (role == null) {
                role = accountRoleRepository.save(this.accountRoleMapper.mapTo(updateDto.getRole()));

            }
            dbAccount.setRole(role);
        }
        if (updateDto.getAccDisplayname() != null) dbAccount.setAccDisplayname(updateDto.getAccDisplayname());

        return accountMapper.mapFrom(accountRepository.save(dbAccount));
    }
}

