package com.restaurant.backend.services.impl;

import com.restaurant.backend.domains.entities.Account;
import com.restaurant.backend.domains.entities.AccountRole;
import com.restaurant.backend.domains.dto.Account.dto.LoginDto;
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

    private final Map<String, String> verificationCodes = new HashMap<>();

    public AccountServiceImpl(AccountRepository accountRepository, AccountRoleRepository accountRoleRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.accountRepository = accountRepository;
        this.accountRoleRepository = accountRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public Account save(Account account) {
        AccountRole accountRole = accountRoleRepository.findByRoleName(account.getRole().getRoleName());
        if (accountRole == null) {
            accountRole = new AccountRole();
            accountRole.setRoleName(account.getRole().getRoleName());
            accountRole = this.accountRoleRepository.save(accountRole);
        }
        account.setRole(accountRole);

        String hashedPassword = passwordEncoder.encode(account.getAccPassword());
        account.setAccPassword(hashedPassword);
        return accountRepository.save(account);
    }
    public boolean checkLogin(LoginDto loginDto) {
        Optional<Account> foundAccount = this.accountRepository.findOneByAccUsername(loginDto.getAccUsername());
        String hashedPassword = passwordEncoder.encode(loginDto.getAccPassword());
        System.out.println(foundAccount);
        if (foundAccount.isPresent()) {
            return passwordEncoder.matches(
                    loginDto.getAccPassword(),
                    foundAccount.get().getAccPassword()
            );
        }
        return false;
    }
    public Account getAccountByUsername(String username){
        Optional<Account> foundAccount = this.accountRepository.findOneByAccUsername(username);
        if (foundAccount.isPresent()) {
            return foundAccount.get();
        }
        return null;
    }

    public Account getAccountByEmail(String email){
        Optional<Account> foundAccount = this.accountRepository.findOneByAccEmail(email);
        if (foundAccount.isPresent()) {
            return foundAccount.get();
        }
        return null;
    }

    public String sendVerificationCode(String email) {
        Optional<Account> foundAccount = this.accountRepository.findOneByAccEmail(email);
        Random random = new Random();
        String code = String.valueOf(random.nextInt(999999));
        if(foundAccount.isPresent()) {
            emailService.sendVerificationCode(email, code);
            verificationCodes.put(email, code);
            return code;
        }
        return null;
    }

    public boolean verify(String email, String code) {
        return code.equals(verificationCodes.get(email));
    }


}
