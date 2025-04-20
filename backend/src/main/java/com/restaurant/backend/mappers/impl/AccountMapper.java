package com.restaurant.backend.mappers.impl;

import com.restaurant.backend.domains.dto.Account.AccountDto;
import com.restaurant.backend.domains.entities.Account;
import com.restaurant.backend.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper implements Mapper<Account, AccountDto> {
    private ModelMapper modelMapper = new ModelMapper();
    public AccountMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public AccountDto mapFrom(Account account) {
        return modelMapper.map(account, AccountDto.class);
    }

    @Override
    public Account mapTo(AccountDto accountDto) {
        return modelMapper.map(accountDto, Account.class);
    }
}
