package com.restaurant.backend.mappers.impl;

import com.restaurant.backend.domains.dto.AccountRole.AccountRoleDto;
import com.restaurant.backend.domains.dto.AccountRole.dto.CreateAccountRoleDto;
import com.restaurant.backend.domains.entities.AccountRole;
import com.restaurant.backend.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AccountRoleMapper implements Mapper<AccountRole, AccountRoleDto> {
    private ModelMapper modelMapper = new ModelMapper();
    public AccountRoleMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    @Override
    public AccountRoleDto mapFrom(AccountRole accountRole) {
        return modelMapper.map(accountRole, AccountRoleDto.class);
    }

    @Override
    public AccountRole mapTo(AccountRoleDto accountRoleDto) {
        return modelMapper.map(accountRoleDto, AccountRole.class);
    }

    public AccountRole mapTo(CreateAccountRoleDto createAccountRoleDto) {
        return modelMapper.map(createAccountRoleDto, AccountRole.class);
    }
    //create DTO k có id => id của Entity là null => Hibernate tự generate id mới khi save
}
