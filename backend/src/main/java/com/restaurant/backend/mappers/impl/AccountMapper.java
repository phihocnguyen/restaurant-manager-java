package com.restaurant.backend.mappers.impl;

import com.restaurant.backend.domains.dto.Account.AccountDto;
import com.restaurant.backend.domains.dto.Account.dto.UpdateAccountDto;
import com.restaurant.backend.domains.entities.Account;
import com.restaurant.backend.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AccountMapper implements Mapper<Account, AccountDto> {
    private ModelMapper modelMapper = new ModelMapper();
    public AccountMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public AccountDto mapFrom(Account account) {
        AccountDto dto = modelMapper.map(account, AccountDto.class);
        dto.setVerified(account.getVerified());
        return dto;
    }

    @Override
    public Account mapTo(AccountDto accountDto) {
        Account account = modelMapper.map(accountDto, Account.class);
        
        // Set default values if not provided
        if (account.getAccBday() == null) {
            account.setAccBday(LocalDate.now());
        }
        if (account.getAccGender() == null) {
            account.setAccGender(false);
        }
        if (account.getAccAddress() == null) {
            account.setAccAddress("");
        }
        if (account.getAccDisplayname() == null) {
            account.setAccDisplayname("User");
        }
        if (account.getIsdeleted() == null) {
            account.setIsdeleted(false);
        }
        account.setVerified(accountDto.getVerified());
        
        return account;
    }

    public Account mapTo(UpdateAccountDto updateAccountDto) {
        return modelMapper.map(updateAccountDto, Account.class);
    }
}
